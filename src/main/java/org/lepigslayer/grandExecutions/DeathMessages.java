package org.lepigslayer.grandExecutions;

import org.bukkit.entity.Entity;
import org.bukkit.event.entity.EntityDamageEvent;

import java.util.*;

public class DeathMessages {
    public static final List<String> attackerMessages = Arrays.asList(
            EntityDamageEvent.DamageCause.ENTITY_ATTACK.toString(),
            EntityDamageEvent.DamageCause.ENTITY_SWEEP_ATTACK.toString(),
            EntityDamageEvent.DamageCause.PROJECTILE.toString(),
            "FALL_NO_ATTACKER",
            "FALL_WITH_ATTACKER"
    );
    public static final String[] summaryMessages = {
            "%s lived a life full of courage and determination, conquering every challenge they faced.",
            "%s was a hero who faced countless battles with unmatched bravery, leaving a legacy behind.",
            "%s journeyed through dangerous lands, earning the respect of all who crossed their path.",
            "%s was known far and wide for their valor, conquering impossible odds and inspiring others.",
            "%s’s life was one of adventure and glory, always charging forward and never backing down.",
            "%s thrived in every challenge, forging a path of victory that will be remembered forever.",
            "%s was a true legend, their name spoken with respect by allies and fear by enemies.",
            "%s’s life was marked by triumphs and feats that will echo in the world for generations.",
            "%s faced every challenge with unmatched bravery, leaving a lasting impact on the world.",
            "%s’s story was one of legendary achievements, making their mark on the world before they fell."
    };
    public static final Map<String, String[]> deathMessages = new HashMap<>() {{
        put("KILL", new String[]{
                "%s met their end in battle, their bravery unmatched but their journey was over.",
                "%s faced their final foe, their strength fading as fate claimed them.",
                "%s was struck down in a battle they could not win, their story now at an end."
        });

        put("WORLD_BORDER", new String[]{
                "%s reached the world border and couldn’t escape its grasp, ending their journey too soon.",
                "%s perished when they hit the world border, unable to survive its pull.",
                "%s’s adventure ended at the edge of the world, claiming their life in the blink of an eye."
        });

        put("CONTACT", new String[]{
                "%s perished in an unexpected encounter, their strength unable to overcome this fatal moment.",
                "%s's life was claimed by an unforeseen force, too quick for even them to escape.",
                "%s met their fate in an unanticipated moment, unable to react before it was too late."
        });

        put("ENTITY_ATTACK", new String[]{
                "%s was struck down by KILLER, their power too great to withstand.",
                "%s fell to KILLER’s attack, their strength unable to hold up.",
                "%s met their end at the hands of KILLER, their defenses no match."
        });

        put("ENTITY_SWEEP_ATTACK", new String[]{
                "%s was swept away by KILLER's attack, their body unable to withstand the force.",
                "%s perished from KILLER’s sweeping blow, their defense too weak.",
                "%s was caught by KILLER’s sweeping attack, their life extinguished in an instant."
        });

        put("PROJECTILE", new String[]{
                "%s was struck by KILLER’s projectile, their life ending swiftly.",
                "%s met their demise by KILLER’s hand, unable to dodge the deadly shot.",
                "%s’s life ended with a single projectile from KILLER, their fate sealed."
        });

        put("SUFFOCATION", new String[]{
                "%s suffocated, the air growing thin as their life was slowly extinguished.",
                "%s couldn't breathe, and suffocation claimed their life in the end.",
                "%s's breath was taken away, and with it, their journey came to an end."
        });

        put("FALL_NO_ATTACKER", new String[]{
                "%s fell to their doom, unable to recover from the height they plummeted from.",
                "%s’s fall ended their life, no chance to survive the deadly descent.",
                "%s's journey ended with a fatal fall, their body unable to withstand the impact."
        });

        put("FALL_WITH_ATTACKER", new String[]{
                "%s was pushed to their doom by %k, unable to recover from the deadly height.",
                "%s fell to their demise after a fateful encounter with %k.",
                "%s's fall was aided by %k, sealing their tragic fate."
        });

        put("FIRE", new String[]{
                "%s burned alive, consumed by the flames that took them too soon.",
                "%s was scorched by the fire, their body reduced to ash.",
                "%s's life was consumed by fire, leaving nothing behind but memories."
        });

        put("FIRE_TICK", new String[]{
                "%s was slowly burned alive by the fire, their fate sealed by the flames.",
                "%s’s life was extinguished by fire, their body unable to withstand the heat.",
                "%s was caught in the fire's grip, their life slipping away with each tick."
        });

        put("MELTING", new String[]{
                "%s melted away, their form disintegrating under the heat.",
                "%s’s body couldn't withstand the heat, melting into nothingness.",
                "%s was undone by the intense heat, their body turning to liquid."
        });

        put("LAVA", new String[]{
                "%s fell into the lava, their body consumed by the searing heat.",
                "%s’s body was incinerated by lava, their life ending in flames.",
                "%s's life was taken by lava, their body destroyed in its fiery embrace."
        });

        put("DROWNING", new String[]{
                "%s drowned, struggling against the water as their life faded.",
                "%s’s last breath was taken by water, drowning them before they could escape.",
                "%s’s life ended as they drowned, the water claiming them too quickly."
        });

        put("BLOCK_EXPLOSION", new String[]{
                "%s was caught in a block explosion, their body shattered by the blast.",
                "%s's life was claimed by an explosion, their body destroyed in an instant.",
                "%s was obliterated by a block explosion, their final moments a blur."
        });

        put("ENTITY_EXPLOSION", new String[]{
                "%s perished in the blast of an entity explosion, their body torn apart.",
                "%s met their end in a devastating explosion, unable to escape the blast radius.",
                "%s was caught in the explosion, their life ending in a single moment."
        });

        put("VOID", new String[]{
                "%s fell into the void, their body lost in an endless abyss.",
                "%s's life ended when they slipped into the void, vanishing without a trace.",
                "%s was swallowed by the void, their journey ending in an eternal fall."
        });

        put("LIGHTNING", new String[]{
                "%s was struck by lightning, their body unable to withstand the surge of power.",
                "%s met their end in a bolt of lightning, their body reduced to nothing.",
                "%s was consumed by a lightning strike, their life ending in a flash."
        });

        put("SUICIDE", new String[]{
                "%s chose to end it all, their journey coming to a tragic close.",
                "%s's life ended by their own hand, leaving only sorrow behind.",
                "%s's fate was sealed by their own choice, their story now over."
        });

        put("STARVATION", new String[]{
                "%s succumbed to hunger, their body weakening as their strength faded away.",
                "%s died from starvation, their body unable to endure the lack of food.",
                "%s's life was claimed by hunger, their body withering from within."
        });

        put("POISON", new String[]{
                "%s was poisoned, their life slowly fading as the venom took hold.",
                "%s succumbed to poison, their body deteriorating until their last breath.",
                "%s’s life was slowly taken by poison, their strength drained away."
        });

        put("MAGIC", new String[]{
                "%s was undone by dark magic, their body overwhelmed by unnatural forces.",
                "%s’s life was taken by magic, their body breaking under its power.",
                "%s fell to the might of magic, their body shattered by its force."
        });

        put("WITHER", new String[]{
                "%s succumbed to the withering effect, their body slowly decaying.",
                "%s’s body withered away, drained of life by an unstoppable force.",
                "%s was drained by the withering effect, their life slipping away."
        });

        put("FALLING_BLOCK", new String[]{
                "%s was crushed by a falling block, their life ending in an instant.",
                "%s’s body was flattened by a falling block, their end swift and final.",
                "%s was struck by a falling block, their journey ending under its weight."
        });

        put("THORNS", new String[]{
                "%s was struck by thorns, their body pierced and their life ended.",
                "%s met their end from the thorns, their body overwhelmed by their sting.",
                "%s's body was pierced by thorns, their life snuffed out instantly."
        });

        put("DRAGON_BREATH", new String[]{
                "%s was consumed by dragon breath, their body reduced to ash.",
                "%s’s life was taken by dragon breath, their body scorched beyond recognition.",
                "%s’s journey ended with the fiery breath of a dragon."
        });

        put("CUSTOM", new String[]{
                "%s's life ended in a way no one could have anticipated.",
                "%s met their end in a custom manner, their fate sealed by the unknown.",
                "%s’s death was as unique as their journey, leaving everyone in shock."
        });

        put("FLY_INTO_WALL", new String[]{
                "%s flew into a wall, their body crashing with fatal force.",
                "%s’s flight ended abruptly when they hit the wall, causing their demise.",
                "%s met their end by flying into a wall, their body too damaged to survive."
        });

        put("HOT_FLOOR", new String[]{
                "%s stepped on a hot floor, their body burned beyond recognition.",
                "%s was burned alive by a hot floor, their journey ending in fire.",
                "%s’s life ended on the hot floor, their body consumed by the flames."
        });

        put("CAMPFIRE", new String[]{
                "%s was burned by a campfire, their body consumed by the flames.",
                "%s’s life was claimed by the campfire, their body charred beyond recognition.",
                "%s met their end in the campfire, their body scorched and destroyed."
        });

        put("CRAMMING", new String[]{
                "%s was crushed by the weight of too many players, their body unable to survive.",
                "%s perished from cramming, their body crushed under the weight.",
                "%s’s life was snuffed out by cramming, their body overwhelmed by others."
        });

        put("DRYOUT", new String[]{
                "%s dried out, their body withering and life fading away.",
                "%s’s life was drained by dehydration, their body succumbing to the lack of water.",
                "%s died from dryout, their body unable to survive without moisture."
        });

        put("FREEZE", new String[]{
                "%s froze to death, their body becoming solid in the cold.",
                "%s’s life ended as they froze, their body becoming brittle and lifeless.",
                "%s succumbed to the freezing cold, their body unable to endure the chill."
        });

        put("SONIC_BOOM", new String[]{
                "%s was struck by a sonic boom, their body torn apart by the shockwave.",
                "%s’s body was crumbled under the immense pressure of a sonic boom",
                "%s was ended by the immense power of a sonic boom, that tore through the air"
        });
    }};

    public static String getRandomMessage(EntityDamageEvent.DamageCause cause, String victim, String killer){
        String sCause = cause.toString();
        if(cause== EntityDamageEvent.DamageCause.FALL){
            sCause = killer.isEmpty() ? "FALL_NO_ATTACKER" : "FALL_WITH_ATTACKER";
        }

        String message = String.format(deathMessages.get(sCause)[new Random().nextInt(3)],victim);

        if(attackerMessages.contains(sCause))
            message = message.replace("KILLER",killer);
        return message;
    }

}
