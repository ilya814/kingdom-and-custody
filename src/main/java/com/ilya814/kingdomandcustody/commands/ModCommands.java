package com.ilya814.kingdomandcustody.commands;

import com.ilya814.kingdomandcustody.registry.ModItems;
import com.ilya814.kingdomandcustody.armor.ArmorTier;
import com.mojang.brigadier.arguments.StringArgumentType;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.equipment.ArmorType;
import net.minecraft.network.chat.Component;

import java.util.Collection;

import static net.minecraft.commands.Commands.argument;
import static net.minecraft.commands.Commands.literal;

public class ModCommands {

    public static void register() {
        CommandRegistrationCallback.EVENT.register((dispatcher, registryAccess, environment) -> {
            dispatcher.register(
                literal("give")
                    .then(argument("player", EntityArgument.players())
                        .then(argument("kit", StringArgumentType.greedyString())
                            .suggests((ctx, builder) -> {
                                builder.suggest("[immortal_kit]");
                                return builder.buildFuture();
                            })
                            .executes(ctx -> {
                                String kit = StringArgumentType.getString(ctx, "kit").trim();
                                Collection<ServerPlayer> players = EntityArgument.getPlayers(ctx, "player");

                                if (!kit.equals("[immortal_kit]")) {
                                    ctx.getSource().sendFailure(Component.literal("Unknown kit: " + kit));
                                    return 0;
                                }

                                for (ServerPlayer player : players) {
                                    // Give all 4 armor pieces
                                    for (ArmorType type : ArmorType.values()) {
                                        var item = ModItems.ARMOR.get(ArmorTier.IMMORTAL).get(type);
                                        if (item != null) {
                                            player.getInventory().add(new ItemStack(item));
                                        }
                                    }
                                    ctx.getSource().sendSuccess(
                                        () -> Component.literal("☠ Gave [immortal_kit] to " + player.getName().getString()),
                                        true
                                    );
                                }
                                return players.size();
                            })
                        )
                    )
            );
        });
    }
}
