package net.deechael.elements.weapon

import net.deechael.elements.api.ElementGauge
import net.deechael.elements.api.ElementService
import net.deechael.elements.api.ElementType
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.NamespacedKey
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.LivingEntity
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import org.bukkit.persistence.PersistentDataType
import org.bukkit.plugin.java.JavaPlugin

class ElementsWeaponPlugin : JavaPlugin(), Listener {

    private lateinit var service: ElementService
    private val elementTypeKey = NamespacedKey("elements", "element_type")

    @EventHandler
    fun event(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if (damager !is LivingEntity)
            return
        val itemInMainHand = damager.equipment!!.itemInMainHand
        if (itemInMainHand.type == Material.AIR)
            return
        val itemMeta = itemInMainHand.itemMeta ?: return
        if (!itemMeta.persistentDataContainer.has(elementTypeKey))
            return
        val elementId = itemMeta.persistentDataContainer.get(elementTypeKey, PersistentDataType.STRING) ?: return
        if (!this.service.hasElementType(elementId))
            return
        val element = this.service.getElementType(elementId)
        this.service.getApplicationManager()
            .doElementalDamage(
                event.entity,
                this.service.getSourceManager().entity(
                    damager,
                    element,
                    ElementGauge(1, 0)
                ),
                event.finalDamage
            )
        event.damage = 0.0
    }

    override fun onEnable() {
        this.service = ElementService.getService()
        Bukkit.getPluginManager().registerEvents(this, this)
        Bukkit.getCommandMap()
            .register("elements", object : Command("weapon_element") {
                override fun tabComplete(
                    sender: CommandSender,
                    alias: String,
                    args: Array<out String>
                ): MutableList<String> {
                    val list = mutableListOf<String>()
                    if (args.size == 1) {
                        this@ElementsWeaponPlugin.service.getElementTypes()
                            .map(ElementType::getId)
                            .forEach(list::add)
                    } else {
                        list.add(" ")
                    }
                    return list.toMutableList()
                }

                override fun execute(sender: CommandSender, commandLabel: String, args: Array<out String>): Boolean {
                    if (args.size == 1) {
                        if (sender !is Player)
                            return true
                        val elementId = args[0]
                        if (!this@ElementsWeaponPlugin.service.hasElementType(elementId))
                            return true
                        val element = this@ElementsWeaponPlugin.service.getElementType(elementId)
                        val itemStack = sender.inventory.itemInMainHand
                        if (itemStack.type == Material.AIR)
                            return true
                        val itemMeta = itemStack.itemMeta ?: return true
                        itemMeta.persistentDataContainer.set(elementTypeKey, PersistentDataType.STRING, element.getId())
                        itemMeta.lore(
                            mutableListOf(
                                Component.empty(),
                                Component.text("Element Applied:")
                                    .decoration(TextDecoration.ITALIC, false)
                                    .color(NamedTextColor.YELLOW)
                                    .decorate(TextDecoration.UNDERLINED),
                                Component.text(element.getId())
                                    .decoration(TextDecoration.ITALIC, false)
                                    .color(TextColor.color(element.getColor().rgb))
                            )
                        )
                        itemStack.itemMeta = itemMeta
                    }
                    return true
                }
            })
    }

}