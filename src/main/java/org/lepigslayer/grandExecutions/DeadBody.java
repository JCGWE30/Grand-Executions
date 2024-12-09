package org.lepigslayer.grandExecutions;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.*;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Wither;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;
import org.bukkit.profile.PlayerTextures;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.util.Vector;
import org.bukkit.util.io.BukkitObjectInputStream;
import org.bukkit.util.io.BukkitObjectOutputStream;

import java.io.*;
import java.util.*;

import static com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction.*;

public class DeadBody {
    private static final String TEAM_NAME = "npcMarkerTeam";
    private static final List<DeadBody> existingBodies = new ArrayList<>();

    private final Block[] digBlocks = new Block[3];

    private Location origin;
    private Vector offset;
    private String name;
    private OfflinePlayer owner;
    private EntityDamageEvent.DamageCause cause;
    private String killer;
    private ItemStack[] inventoryContents;
    private WrappedGameProfile fakeProfile;
    private ItemStack head;
    private List<UUID> watchers = new ArrayList<>();

    public DeadBody(Player owner, Location location, EntityDamageEvent.DamageCause cause,String killer) {
        existingBodies.add(this);
        this.owner = owner;
        this.name = reverse(owner.getName());
        this.cause = cause;
        this.killer = killer;
        this.origin = location;
        this.offset = new Vector(0.45, 0.08, 0);
        this.fakeProfile = getFakeProfile();
        setInventory();
        setHead(owner.getPlayer());
        ensureTeam();
        setBlocks(true);
        spawnBody();
        GrandExecutions.getSaver().saveCorpse(this,owner.getUniqueId().toString());
    }

    private DeadBody(){};

    private void setBlocks(boolean hard) {
        digBlocks[0] = origin.getBlock().getRelative(BlockFace.DOWN);
        digBlocks[1] = digBlocks[0].getRelative(BlockFace.WEST);
        digBlocks[2] = origin.getBlock().getRelative(BlockFace.WEST);

        if(!hard) return;

        ItemStack pickaxe = new ItemStack(Material.DIAMOND_PICKAXE);

        List<ItemStack> drops = new ArrayList<>(Arrays.stream(inventoryContents).toList());

        for (Block b : digBlocks) {
            drops.addAll(b.getDrops(pickaxe));
            b.setType(Material.COARSE_DIRT);
        }

        inventoryContents = drops.toArray(new ItemStack[drops.size()]);

        digBlocks[2].setType(Material.AIR);
    }

    private void setInventory(){
        List<ItemStack> items = new ArrayList<>();
        for(ItemStack i:owner.getPlayer().getInventory().getContents()){
            if(i==null) continue;
            ItemStack ni = new ItemStack(i);
            items.add(ni);
        }
        inventoryContents = items.toArray(ItemStack[]::new);
    }

    private void ensureTeam() {
        Scoreboard board = Bukkit.getScoreboardManager().getMainScoreboard();

        if (board.getTeam(TEAM_NAME) != null) return;

        Team team = board.registerNewTeam(TEAM_NAME);
        team.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);
    }

    private void spawnBody() {
        for (Player p : Bukkit.getOnlinePlayers()) {
            syncBody(p);
        }
    }

    private void deLoad(Player p) {
        PacketContainer removePlayerPacket = new PacketContainer(PacketType.Play.Server.ENTITY_DESTROY);
        PacketContainer removeInfoPacket = new PacketContainer(PacketType.Play.Server.PLAYER_INFO_REMOVE);

        removePlayerPacket.getModifier().write(0, new IntArrayList(new int[]{fakeProfile.getUUID().hashCode()}));
        removeInfoPacket.getUUIDLists().write(0, Collections.singletonList(fakeProfile.getUUID()));

        ProtocolLibrary.getProtocolManager().sendServerPacket(p, removePlayerPacket);
        ProtocolLibrary.getProtocolManager().sendServerPacket(p, removeInfoPacket);
    }

    private void syncBody(Player player) {
        watchers.add(player.getUniqueId());

        PacketContainer addPacket = new PacketContainer(PacketType.Play.Server.PLAYER_INFO);
        PacketContainer spawnPacket = new PacketContainer(PacketType.Play.Server.SPAWN_ENTITY);
        PacketContainer sleepPacket = new PacketContainer(PacketType.Play.Server.ENTITY_METADATA);
        PacketContainer teamPacket = new PacketContainer(PacketType.Play.Server.SCOREBOARD_TEAM);

        PlayerInfoData data =
                new PlayerInfoData(fakeProfile.getUUID(), 0, false, EnumWrappers.NativeGameMode.SURVIVAL, fakeProfile, WrappedChatComponent.fromText(""));

        addPacket.getPlayerInfoActions().write(0, Set.of(ADD_PLAYER));
        addPacket.getPlayerInfoDataLists().write(1, Collections.singletonList(data));

        spawnPacket.getIntegers().write(0, fakeProfile.getUUID().hashCode());
        spawnPacket.getUUIDs().write(0, fakeProfile.getUUID());
        spawnPacket.getEntityTypeModifier().write(0, EntityType.PLAYER);

        Location trueLoc = getTrueLocation();

        spawnPacket.getDoubles().write(0, trueLoc.getX())
                .write(1, trueLoc.getY())
                .write(2, trueLoc.getZ());
        spawnPacket.getBytes()
                .write(0, (byte) 0)
                .write(1, (byte) 0);

        sleepPacket.getIntegers().write(0, fakeProfile.getUUID().hashCode());
        sleepPacket.getDataValueCollectionModifier().write(0,Arrays.asList(
                new WrappedDataValue(6, WrappedDataWatcher.Registry.get(EnumWrappers.getEntityPoseClass()), EnumWrappers.EntityPose.SLEEPING.toNms()),
                new WrappedDataValue(17, WrappedDataWatcher.Registry.get(Byte.class), (byte) 0x7F)
                )
        );



        teamPacket.getIntegers().write(0, 3);
        teamPacket.getStrings().write(0,TEAM_NAME);
        teamPacket.getSpecificModifier(Collection.class).write(0,Collections.singletonList(name));

        ProtocolLibrary.getProtocolManager().sendServerPacket(player, addPacket);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, spawnPacket);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, sleepPacket);
        ProtocolLibrary.getProtocolManager().sendServerPacket(player, teamPacket);
    }

    private void destroyCorpse() {
        existingBodies.remove(this);

        for (UUID id : watchers) {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(id);
            if (offline.isOnline()) {
                deLoad(offline.getPlayer());
            }
        }
    }

    private WrappedGameProfile getFakeProfile() {
        UUID fakeId = UUID.randomUUID();
        WrappedGameProfile gameProfile = new WrappedGameProfile(fakeId, name);
        gameProfile.getProperties().putAll(WrappedGameProfile.fromPlayer(owner.getPlayer()).getProperties());
        return gameProfile;
    }

    private static String reverse(String string) {
        StringBuilder builder = new StringBuilder();
        for (int i = string.length() - 1; i >= 0; i--) {
            builder.append(string.charAt(i));
        }
        return builder.toString();
    }

    private boolean checkHit(Location hitLocation) {
        if (!Arrays.stream(digBlocks).toList().contains(hitLocation.getBlock())) return false;

        destroyCorpse();
        for (ItemStack item : inventoryContents) {
            if (item == null) continue;
            origin.getWorld().dropItemNaturally(origin, item);
        }
        origin.getWorld().dropItemNaturally(origin,head);
        origin.getWorld().dropItemNaturally(origin,EulogyManager.getEulogy(cause,owner,killer));
        origin.getWorld().spawnParticle(Particle.POOF, origin, 0);
        origin.getWorld().playSound(origin, Sound.ENTITY_HORSE_SADDLE, 1, 1);
        GrandExecutions.getSaver().deleteCorpse(owner.getUniqueId().toString());

        return true;
    }

    private Location getTrueLocation() {
        return origin.clone().add(offset);
    }

    public static boolean checkHits(Location location) {
        for (DeadBody body : existingBodies) {
            if (body.checkHit(location)) return true;
        }
        return false;
    }

    public static void loadCorpses(){
        existingBodies.addAll(GrandExecutions.getSaver().loadCorpses());
    }

    public static void syncCorpses(Player p) {
        for (DeadBody body : existingBodies) {
            body.deLoad(p);
            body.syncBody(p);
        }
    }

    public void setOffset(Vector offset) {
        this.offset = offset;
        for (UUID p : watchers.stream().toList()) {
            OfflinePlayer offline = Bukkit.getOfflinePlayer(p);
            if (offline.isOnline()) {
                deLoad(offline.getPlayer());
                syncBody(offline.getPlayer());
            }
        }
    }

    public static DeadBody getFirst() {
        return existingBodies.getFirst();
    }

    public static boolean isCorpse(WrappedGameProfile p){
        for(DeadBody body : existingBodies){
            if(body.fakeProfile.getUUID()==p.getUUID()) return true;
        }
        return false;
    }

    private void setHead() {
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        PlayerProfile skullProfile = Bukkit.createPlayerProfile(owner.getName());
        skullProfile.getTextures().setSkin(HeadUtils.fetchTexture(owner.getUniqueId()));
        meta.setOwnerProfile(skullProfile);
        item.setItemMeta(meta);
        head = item;
    }

    private void setHead(Player p){
        ItemStack item = new ItemStack(Material.PLAYER_HEAD, 1);
        SkullMeta meta = (SkullMeta) item.getItemMeta();
        meta.setOwnerProfile(p.getPlayerProfile());
        item.setItemMeta(meta);
        head = item;
    }

    public byte[] serialize(){
        try(ByteArrayOutputStream byteOut = new ByteArrayOutputStream()){
            BukkitObjectOutputStream objectOut = new BukkitObjectOutputStream(byteOut);

            objectOut.writeObject(origin.getWorld().getUID());

            objectOut.writeObject(SerializableVector.of(origin.toVector()));

            objectOut.writeObject(SerializableVector.of(offset));

            objectOut.writeObject(name);

            objectOut.writeObject(cause);

            objectOut.writeObject(killer);

            objectOut.writeObject(owner.getUniqueId());

//            List<Map<String,Object>> items = new ArrayList<>();
//
//            for(ItemStack item : inventoryContents){
//                items.add(item.serialize());
//            }
//
//            objectOut.writeObject(items.toArray(Map[]::new));
            objectOut.writeObject(inventoryContents);

            Collection<WrappedSignedProperty> properties = fakeProfile.getProperties().get("textures");
            WrappedSignedProperty textures = properties.iterator().next();

            objectOut.writeObject(textures.getSignature());
            objectOut.writeObject(textures.getValue());

            return byteOut.toByteArray();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static DeadBody deseralize(byte[] bytes){
        try(ByteArrayInputStream byteIn = new ByteArrayInputStream(bytes)){
            BukkitObjectInputStream objectIn = new BukkitObjectInputStream(byteIn);
            DeadBody body = new DeadBody();

            World world = Bukkit.getWorld((UUID) objectIn.readObject());

            body.origin = SerializableVector.as(objectIn.readObject()).toLocation(world);
            body.offset = SerializableVector.as(objectIn.readObject());
            body.name = (String) objectIn.readObject();
            body.cause = (EntityDamageEvent.DamageCause) objectIn.readObject();
            body.killer = (String) objectIn.readObject();
            body.owner = Bukkit.getOfflinePlayer((UUID) objectIn.readObject());
//            Map<String,Object>[] serializeMap = (Map<String,Object>[]) objectIn.readObject();
//
//            List<ItemStack> items = new ArrayList<>();
//
//            for(Map<String,Object> serializedItem: serializeMap){
//                items.add(ItemStack.deserialize(serializedItem));
//            }
//
//            body.inventoryContents = items.toArray(ItemStack[]::new);
            body.inventoryContents = (ItemStack[]) objectIn.readObject();

            String signature = (String) objectIn.readObject();
            String value = (String) objectIn.readObject();

            WrappedSignedProperty property = new WrappedSignedProperty("textures",value,signature);

            WrappedGameProfile profile = new WrappedGameProfile(UUID.randomUUID(),body.name);

            profile.getProperties().put("textures",property);
            body.fakeProfile = profile;

            body.ensureTeam();
            body.setBlocks(false);
            body.setHead();

            return body;
        }catch(IOException | ClassNotFoundException e){
            throw new RuntimeException(e);
        }
    }
}
