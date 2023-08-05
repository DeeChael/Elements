package net.deechael.elements.core.registry

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementService
import net.deechael.elements.api.application.source.ApplicationSource
import net.deechael.elements.api.application.source.EntitySource
import net.deechael.elements.api.reaction.ElementReaction
import net.deechael.elements.api.reaction.ElementReactionTrigger
import net.deechael.elements.core.ElementsPlugin
import net.deechael.elements.core.util.Colors
import org.bukkit.entity.Creeper
import org.bukkit.entity.Entity
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DefaultElementReactionRegistry {

    @JvmStatic
    fun registerAll(service: ElementService) {
        registerPyroAll(service)
        registerHydroAll(service)
        registerElectroAll(service)
        registerCryoAll(service)
        registerDendroAll(service)
        registerAnemoAll(service)
        registerGeoAll(service)
        registerReacterAll(service)
    }

    private fun registerPyroAll(service: ElementService) {
        service.registerElementReaction(
            "vaporize",
            Colors.Reactions.VAPORIZE,
            DefaultElementTypeRegistry.PYRO,
            arrayOf(DefaultElementTypeRegistry.HYDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.fireTicks = 0
                }

                override fun triggerDamage(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge,
                    originalDamage: Double
                ): Double {
                    return originalDamage * 2.0
                }
            }
        )
        service.registerElementReaction(
            "overloaded",
            Colors.Reactions.OVERLOADED,
            DefaultElementTypeRegistry.PYRO,
            arrayOf(DefaultElementTypeRegistry.ELECTRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource is EntitySource) {
                        sufferer.location.world.createExplosion(applicationSource.getEntity(), sufferer.location, 2.0f)
                    } else {
                        sufferer.location.world.createExplosion(sufferer.location, 2.0f)
                    }
                }
            }
        )
        service.registerElementReaction(
            "melt",
            Colors.Reactions.MELT,
            DefaultElementTypeRegistry.PYRO,
            arrayOf(DefaultElementTypeRegistry.CRYO),
            object : ElementReactionTrigger {
                override fun triggerDamage(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge,
                    originalDamage: Double
                ): Double {
                    return originalDamage * 1.5
                }
            }
        )
        service.registerElementReaction(
            "burning",
            Colors.Reactions.BURNING,
            DefaultElementTypeRegistry.PYRO,
            arrayOf(DefaultElementTypeRegistry.DENDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.fireTicks = 10 * 20
                }
            }
        )
        service.registerElementReaction(
            "swirl_pyro",
            Colors.Reactions.SWIRL,
            DefaultElementTypeRegistry.PYRO,
            arrayOf(DefaultElementTypeRegistry.ANEMO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0)
                        .forEach {
                            it.fireTicks = 3 * 20
                            service.getApplicationManager().getApplication(it)
                                .applyElement(
                                    ElementsPlugin.getInstance()
                                        .getSourceManager()
                                        .reaction(
                                            reaction,
                                            DefaultElementTypeRegistry.PYRO,
                                            formerElementTypeGauge.multiple(0.6)
                                        )
                                )
                        }
                }
            }
        )
        service.registerElementReaction(
            "crystallize_pyro",
            Colors.Reactions.CRYSTALLIZE,
            DefaultElementTypeRegistry.PYRO,
            arrayOf(DefaultElementTypeRegistry.GEO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource !is EntitySource)
                        return
                    (applicationSource.getEntity() as LivingEntity).addPotionEffect(
                        PotionEffect(
                            PotionEffectType.DAMAGE_RESISTANCE,
                            3 * 20,
                            1,
                        )
                    )
                }
            }
        )
    }

    private fun registerHydroAll(service: ElementService) {
        service.registerElementReaction(
            "vaporize_reverse",
            Colors.Reactions.VAPORIZE,
            DefaultElementTypeRegistry.HYDRO,
            arrayOf(DefaultElementTypeRegistry.PYRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.fireTicks = 0
                }

                override fun triggerDamage(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge,
                    originalDamage: Double
                ): Double {
                    return originalDamage * 1.5
                }
            }
        )
        service.registerElementReaction(
            "electro_charged",
            Colors.Reactions.ELECTRO_CHARGED,
            DefaultElementTypeRegistry.HYDRO,
            arrayOf(DefaultElementTypeRegistry.ELECTRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (!sufferer.isInWater)
                        return
                    val damage = PotionEffect(
                        PotionEffectType.HARM,
                        1 * 20,
                        1
                    )
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0)
                        .filter(Entity::isInWater)
                        .map { it as LivingEntity }
                        .forEach {
                            it.addPotionEffect(damage)
                            if (it is Creeper)
                                it.isPowered = true
                        }
                }
            }
        )
        service.registerElementReaction(
            "frozen",
            Colors.Reactions.FROZEN,
            DefaultElementTypeRegistry.HYDRO,
            arrayOf(DefaultElementTypeRegistry.CRYO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer as LivingEntity
                    sufferer.addPotionEffects(
                        listOf(
                            PotionEffect(
                                PotionEffectType.SLOW,
                                5 * 20,
                                255
                            ),
                            PotionEffect(
                                PotionEffectType.SLOW_DIGGING,
                                5 * 20,
                                255
                            ),
                            PotionEffect(
                                PotionEffectType.WEAKNESS,
                                5 * 20,
                                255
                            ),
                            PotionEffect(
                                PotionEffectType.JUMP,
                                5 * 20,
                                128 // Over 127 will make player not able to jump
                            )
                        )
                    )
                }
            }
        )
        service.registerElementReaction(
            "bloom",
            Colors.Reactions.BLOOM,
            DefaultElementTypeRegistry.HYDRO,
            arrayOf(DefaultElementTypeRegistry.DENDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    service.getApplicationManager().getApplication(sufferer)
                        .applyElement(
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.REACTER_BLOOM,
                                    ElementGauge(1)
                                )
                        )
                }
            }
        )
        service.registerElementReaction(
            "swirl_hydro",
            Colors.Reactions.SWIRL,
            DefaultElementTypeRegistry.HYDRO,
            arrayOf(DefaultElementTypeRegistry.ANEMO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0)
                        .forEach {
                            service.getApplicationManager().getApplication(it)
                                .applyElement(
                                    ElementsPlugin.getInstance()
                                        .getSourceManager()
                                        .reaction(
                                            reaction,
                                            DefaultElementTypeRegistry.HYDRO,
                                            formerElementTypeGauge.multiple(0.6)
                                        )
                                )
                        }
                }
            }
        )
        service.registerElementReaction(
            "crystallize_hydro",
            Colors.Reactions.CRYSTALLIZE,
            DefaultElementTypeRegistry.HYDRO,
            arrayOf(DefaultElementTypeRegistry.GEO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource !is EntitySource)
                        return
                    (applicationSource.getEntity() as LivingEntity).addPotionEffect(
                        PotionEffect(
                            PotionEffectType.DAMAGE_RESISTANCE,
                            3 * 20,
                            1,
                        )
                    )
                }
            }
        )
    }

    private fun registerElectroAll(service: ElementService) {
        service.registerElementReaction(
            "overloaded_reverse",
            Colors.Reactions.OVERLOADED,
            DefaultElementTypeRegistry.ELECTRO,
            arrayOf(DefaultElementTypeRegistry.PYRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource is EntitySource) {
                        sufferer.location.world.createExplosion(applicationSource.getEntity(), sufferer.location, 2.0f)
                    } else {
                        sufferer.location.world.createExplosion(sufferer.location, 2.0f)
                    }
                }
            }
        )
        service.registerElementReaction(
            "electro_charged_reverse",
            Colors.Reactions.ELECTRO_CHARGED,
            DefaultElementTypeRegistry.ELECTRO,
            arrayOf(DefaultElementTypeRegistry.HYDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (!sufferer.isInWater)
                        return
                    val damage = PotionEffect(
                        PotionEffectType.HARM,
                        1 * 20,
                        1
                    )
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0)
                        .filter(Entity::isInWater)
                        .map { it as LivingEntity }
                        .forEach {
                            it.addPotionEffect(damage)
                            if (it is Creeper)
                                it.isPowered = true
                        }
                }
            }
        )
        service.registerElementReaction(
            "super_conduct",
            Colors.Reactions.SUPER_CONDUCT,
            DefaultElementTypeRegistry.ELECTRO,
            arrayOf(DefaultElementTypeRegistry.CRYO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    val weak = PotionEffect(
                        PotionEffectType.WEAKNESS,
                        12 * 20,
                        5
                    )
                    sufferer.getNearbyEntities(5.0, 5.0, 5.0)
                        .let {
                            return@let if (applicationSource !is EntitySource) it else it.filter { entity ->
                                entity != applicationSource.getEntity()
                            }
                        }
                        .map { it as LivingEntity }
                        .forEach {
                            service.getApplicationManager().getApplication(it)
                                .applyElement(
                                    ElementsPlugin.getInstance()
                                        .getSourceManager()
                                        .reaction(
                                            reaction,
                                            DefaultElementTypeRegistry.CRYO,
                                            formerElementTypeGauge.multiple(0.6)
                                        )
                                )
                            it.addPotionEffect(weak)
                        }
                }
            }
        )
        service.registerElementReaction(
            "quicken",
            Colors.Reactions.SUPER_CONDUCT,
            DefaultElementTypeRegistry.ELECTRO,
            arrayOf(DefaultElementTypeRegistry.DENDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    service.getApplicationManager().getApplication(sufferer)
                        .applyElement(
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.REACTER_QUICKEN,
                                    ElementGauge(1)
                                )
                        )
                }
            }
        )
        service.registerElementReaction(
            "swirl_electro",
            Colors.Reactions.SWIRL,
            DefaultElementTypeRegistry.ELECTRO,
            arrayOf(DefaultElementTypeRegistry.ANEMO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0)
                        .forEach {
                            service.getApplicationManager().getApplication(it)
                                .applyElement(
                                    ElementsPlugin.getInstance()
                                        .getSourceManager()
                                        .reaction(
                                            reaction,
                                            DefaultElementTypeRegistry.ELECTRO,
                                            formerElementTypeGauge.multiple(0.6)
                                        )
                                )
                        }
                }
            }
        )
        service.registerElementReaction(
            "crystallize_electro",
            Colors.Reactions.CRYSTALLIZE,
            DefaultElementTypeRegistry.ELECTRO,
            arrayOf(DefaultElementTypeRegistry.GEO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource !is EntitySource)
                        return
                    (applicationSource.getEntity() as LivingEntity).addPotionEffect(
                        PotionEffect(
                            PotionEffectType.DAMAGE_RESISTANCE,
                            3 * 20,
                            1,
                        )
                    )
                }
            }
        )
    }

    private fun registerCryoAll(service: ElementService) {
        service.registerElementReaction(
            "melt_reverse",
            Colors.Reactions.MELT,
            DefaultElementTypeRegistry.CRYO,
            arrayOf(DefaultElementTypeRegistry.PYRO),
            object : ElementReactionTrigger {
                override fun triggerDamage(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge,
                    originalDamage: Double
                ): Double {
                    return originalDamage * 2.0
                }
            }
        )
        service.registerElementReaction(
            "frozen_reverse",
            Colors.Reactions.FROZEN,
            DefaultElementTypeRegistry.CRYO,
            arrayOf(DefaultElementTypeRegistry.HYDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer as LivingEntity
                    sufferer.addPotionEffects(
                        listOf(
                            PotionEffect(
                                PotionEffectType.SLOW,
                                5 * 20,
                                255
                            ),
                            PotionEffect(
                                PotionEffectType.SLOW_DIGGING,
                                5 * 20,
                                255
                            ),
                            PotionEffect(
                                PotionEffectType.WEAKNESS,
                                5 * 20,
                                255
                            ),
                            PotionEffect(
                                PotionEffectType.JUMP,
                                5 * 20,
                                128 // Over 127 will make player not able to jump
                            )
                        )
                    )
                }
            }
        )
        service.registerElementReaction(
            "super_conduct_reverse",
            Colors.Reactions.SUPER_CONDUCT,
            DefaultElementTypeRegistry.CRYO,
            arrayOf(DefaultElementTypeRegistry.ELECTRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    val weak = PotionEffect(
                        PotionEffectType.WEAKNESS,
                        12 * 20,
                        5
                    )
                    sufferer.getNearbyEntities(5.0, 5.0, 5.0)
                        .let {
                            return@let if (applicationSource !is EntitySource) it else it.filter { entity ->
                                entity != applicationSource.getEntity()
                            }
                        }
                        .map { it as LivingEntity }
                        .forEach {
                            service.getApplicationManager().getApplication(it)
                                .applyElement(
                                    ElementsPlugin.getInstance()
                                        .getSourceManager()
                                        .reaction(
                                            reaction,
                                            DefaultElementTypeRegistry.CRYO,
                                            formerElementTypeGauge.multiple(0.6)
                                        )
                                )
                            it.addPotionEffect(weak)
                        }
                }
            }
        )
        service.registerElementReaction(
            "swirl_cryo",
            Colors.Reactions.SWIRL,
            DefaultElementTypeRegistry.CRYO,
            arrayOf(DefaultElementTypeRegistry.ANEMO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0)
                        .forEach {
                            service.getApplicationManager().getApplication(it)
                                .applyElement(
                                    ElementsPlugin.getInstance()
                                        .getSourceManager()
                                        .reaction(
                                            reaction,
                                            DefaultElementTypeRegistry.CRYO,
                                            formerElementTypeGauge.multiple(0.6)
                                        )
                                )
                        }
                }
            }
        )
        service.registerElementReaction(
            "crystallize_cryo",
            Colors.Reactions.CRYSTALLIZE,
            DefaultElementTypeRegistry.CRYO,
            arrayOf(DefaultElementTypeRegistry.GEO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource !is EntitySource)
                        return
                    (applicationSource.getEntity() as LivingEntity).addPotionEffect(
                        PotionEffect(
                            PotionEffectType.DAMAGE_RESISTANCE,
                            3 * 20,
                            1,
                        )
                    )
                }
            }
        )
    }

    private fun registerDendroAll(service: ElementService) {
        service.registerElementReaction(
            "burning_reverse",
            Colors.Reactions.BURNING,
            DefaultElementTypeRegistry.DENDRO,
            arrayOf(DefaultElementTypeRegistry.PYRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.fireTicks = 10 * 20
                }
            }
        )
        service.registerElementReaction(
            "bloom_reverse",
            Colors.Reactions.BLOOM,
            DefaultElementTypeRegistry.DENDRO,
            arrayOf(DefaultElementTypeRegistry.HYDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    service.getApplicationManager().getApplication(sufferer)
                        .applyElement(
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.REACTER_BLOOM,
                                    ElementGauge(1)
                                )
                        )
                }
            }
        )
        service.registerElementReaction(
            "quicken_reverse",
            Colors.Reactions.SUPER_CONDUCT,
            DefaultElementTypeRegistry.DENDRO,
            arrayOf(DefaultElementTypeRegistry.ELECTRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    service.getApplicationManager().getApplication(sufferer)
                        .applyElement(
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.REACTER_QUICKEN,
                                    ElementGauge(1)
                                )
                        )
                }
            }
        )
    }

    private fun registerAnemoAll(service: ElementService) {
        service.registerElementReaction(
            "swirl",
            Colors.Reactions.SWIRL,
            DefaultElementTypeRegistry.ANEMO,
            arrayOf(
                DefaultElementTypeRegistry.PYRO,
                DefaultElementTypeRegistry.HYDRO,
                DefaultElementTypeRegistry.ELECTRO,
                DefaultElementTypeRegistry.CRYO
            ),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    val newReaction = service.getElementReaction("swirl_${applicationSource.getElementType().getId()}")
                    newReaction.getTrigger().trigger(reaction, applicationSource, sufferer, formerElementTypeGauge)
                }
            }
        )
    }

    private fun registerGeoAll(service: ElementService) {
        service.registerElementReaction(
            "crystallize",
            Colors.Reactions.CRYSTALLIZE,
            DefaultElementTypeRegistry.GEO,
            arrayOf(
                DefaultElementTypeRegistry.PYRO,
                DefaultElementTypeRegistry.HYDRO,
                DefaultElementTypeRegistry.ELECTRO,
                DefaultElementTypeRegistry.CRYO
            ),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    if (applicationSource !is EntitySource)
                        return
                    (applicationSource.getEntity() as LivingEntity).addPotionEffect(
                        PotionEffect(
                            PotionEffectType.DAMAGE_RESISTANCE,
                            3 * 20,
                            1,
                        )
                    )
                }
            }
        )
    }

    private fun registerReacterAll(service: ElementService) {
        service.registerElementReaction(
            "burgeon",
            Colors.Reactions.BURGEON,
            DefaultElementTypeRegistry.REACTER_BLOOM,
            arrayOf(DefaultElementTypeRegistry.PYRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(5.0, 5.0, 5.0).forEach {
                        service.getApplicationManager().doElementalDamage(
                            it,
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.DENDRO,
                                    ElementGauge(1)
                                ),
                            formerElementTypeGauge.toDouble() * 2
                        )
                    }
                }
            }
        )
        service.registerElementReaction(
            "hyperbloom",
            Colors.Reactions.BURGEON,
            DefaultElementTypeRegistry.REACTER_BLOOM,
            arrayOf(DefaultElementTypeRegistry.ELECTRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(2.0, 2.0, 2.0).forEach {
                        service.getApplicationManager().doElementalDamage(
                            it,
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.DENDRO,
                                    ElementGauge(1)
                                ),
                            formerElementTypeGauge.toDouble() * 2
                        )
                    }
                    sufferer.location.world.createExplosion(sufferer.location, 2.0f)
                }
            }
        )
        service.registerElementReaction(
            "aggravate",
            Colors.Reactions.AGGRAVATE,
            DefaultElementTypeRegistry.REACTER_QUICKEN,
            arrayOf(DefaultElementTypeRegistry.ELECTRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.location.world.createExplosion(sufferer.location, 2.0f)
                }

                override fun triggerDamage(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge,
                    originalDamage: Double
                ): Double {
                    return originalDamage * 1.15
                }
            }
        )
        service.registerElementReaction(
            "spread",
            Colors.Reactions.SPREAD,
            DefaultElementTypeRegistry.REACTER_QUICKEN,
            arrayOf(DefaultElementTypeRegistry.DENDRO),
            object : ElementReactionTrigger {
                override fun trigger(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge
                ) {
                    sufferer.getNearbyEntities(3.0, 3.0, 3.0).forEach {
                        service.getApplicationManager().doElementalDamage(
                            it,
                            ElementsPlugin.getInstance()
                                .getSourceManager()
                                .reaction(
                                    reaction,
                                    DefaultElementTypeRegistry.DENDRO,
                                    ElementGauge(1)
                                ),
                            formerElementTypeGauge.toDouble() * 1.5
                        )
                    }
                }

                override fun triggerDamage(
                    reaction: ElementReaction,
                    applicationSource: ApplicationSource,
                    sufferer: Entity,
                    formerElementTypeGauge: ElementGauge,
                    originalDamage: Double
                ): Double {
                    return originalDamage * 1.25
                }
            }
        )
    }

}