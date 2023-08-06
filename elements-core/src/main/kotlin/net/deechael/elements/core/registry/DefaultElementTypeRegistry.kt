package net.deechael.elements.core.registry

import net.deechael.elements.api.ElementService
import net.deechael.elements.api.ElementType
import net.deechael.elements.core.util.Colors
import org.bukkit.entity.LivingEntity
import org.bukkit.potion.PotionEffect
import org.bukkit.potion.PotionEffectType

object DefaultElementTypeRegistry {

    lateinit var PYRO: ElementType
        private set
    lateinit var HYDRO: ElementType
        private set
    lateinit var ANEMO: ElementType
        private set
    lateinit var ELECTRO: ElementType
        private set
    lateinit var DENDRO: ElementType
        private set
    lateinit var CRYO: ElementType
        private set
    lateinit var GEO: ElementType
        private set

    lateinit var REACTER_BLOOM: ElementType
        private set
    lateinit var REACTER_QUICKEN: ElementType
        private set

    @JvmStatic
    fun registerAll(service: ElementService) {
        this.PYRO = service.registerElementType(
            "pyro",
            "🔥",
            Colors.Elements.PYRO,
            true,
            true
        ) { source, sufferer ->
            sufferer.fireTicks = (source.getElementGauge().toDouble() * 60).toInt()
        }
        this.HYDRO = service.registerElementType(
            "hydro",
            "\uD83C\uDF0A",
            Colors.Elements.HYDRO,
            true,
            true
        )
        this.ANEMO = service.registerElementType(
            "anemo",
            "🍃",
            Colors.Elements.ANEMO,
            false,
            true
        )
        this.ELECTRO = service.registerElementType(
            "electro",
            "⚡",
            Colors.Elements.ELECTRO,
            true,
            true
        )
        this.DENDRO = service.registerElementType(
            "dendro",
            "🍀",
            Colors.Elements.DENDRO,
            true,
            true
        )
        this.CRYO = service.registerElementType(
            "cryo",
            "🧊",
            Colors.Elements.CRYO,
            true,
            true
        ) { source, sufferer ->
            sufferer as LivingEntity
            sufferer.addPotionEffect(
                PotionEffect(
                    PotionEffectType.SLOW,
                    (10 * source.getElementGauge().toDouble()).toInt() * 20,
                    1
                )
            )
        }
        this.GEO = service.registerElementType(
            "geo",
            "🪨",
            Colors.Elements.GEO,
            false,
            true
        )

        this.REACTER_BLOOM = service.registerElementType(
            "reacter_bloom",
            "",
            Colors.Elements.DENDRO,
            true,
            true,
            true
        )
        this.REACTER_QUICKEN = service.registerElementType(
            "reacter_quicken",
            "",
            Colors.Elements.DENDRO,
            true,
            true,
            true
        )
    }

}