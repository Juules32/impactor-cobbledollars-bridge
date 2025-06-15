/*
 * This file is part of Impactor, licensed under the MIT License (MIT).
 *
 * Copyright (c) 2018-2022 NickImpact
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 *
 */

package net.impactdev.impactor.minecraft.api.items;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;

import java.lang.reflect.Method;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.UUID;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.exceptions.CommandSyntaxException;

public class CobbleDollarsBridge {
    private static MinecraftServer getServer() {
        return ServerProvider.server();
    }

    private static String getPlayerName(UUID uuid) {
        ServerPlayer player = getPlayer(uuid);
        return player.getGameProfile().getName();
    }

    private static ServerPlayer getPlayer(UUID uuid) {
        return getServer().getPlayerList().getPlayer(uuid);
    }

    public static BigDecimal getCobbleDollarsReflect(ServerPlayer player) {
        try {
            // Black magic... (In reality it's because there are some missing kotlin mappings or smth)
            Class<?> extClass = Class.forName("fr.harmex.cobbledollars.common.utils.extensions.PlayerExtensionKt");
            Method method = extClass.getMethod("getCobbleDollars", Player.class);
            return new BigDecimal((BigInteger) method.invoke(null, player));
        } catch (Exception e) {
            e.printStackTrace();
            return BigDecimal.ZERO;
        }
    }

    public static BigDecimal balance(UUID uuid) {
        return getCobbleDollarsReflect(getPlayer(uuid));
    }

    private static CommandDispatcher<CommandSourceStack> getCommandDispatcher() {
        return getServer().createCommandSourceStack().withSuppressedOutput().dispatcher();
    }

    public static void executeCommand(String command) {
        try {
            getCommandDispatcher().execute(command, getServer().createCommandSourceStack());
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }

    public static void reset(UUID owner) {
        set(owner, new BigDecimal(0));
    }

    public static void set(UUID owner, BigDecimal amount) {
        String playerName = getPlayerName(owner);
        executeCommand("cobbledollars set " + playerName + " " + amount);
    }

    public static void withdraw(UUID owner, BigDecimal amount) {
        String playerName = getPlayerName(owner);
        executeCommand("cobbledollars remove " + playerName + " " + amount);
    }
    
    public static void deposit(UUID owner, BigDecimal amount) {
        String playerName = getPlayerName(owner);
        executeCommand("cobbledollars give " + playerName + " " + amount);
    }

    public static void transfer(UUID from, UUID to, BigDecimal amount) {
        CommandSourceStack fromSource = getPlayer(from).createCommandSourceStack();
        String toName = getPlayerName(to);
        String command = "cobbledollars give " + toName + " " + amount;
        try {
            getCommandDispatcher().execute(command, fromSource);
        } catch (CommandSyntaxException e) {
            e.printStackTrace();
        }
    }
}

// RANDOM STUFF THAT MIGHT BE USEFUL:

// return getServer().getProfileCache().get();
//ServerPlayer player = getPlayer(uuid);

// LOGGER.info("Balance: " + PlayerExtensionKt.getCobbleDollars((Player) player));

//CobbleDollars.INSTANCE.getCobbleDollarsAccounts().

//LOGGER.info("Balance: " + PlayerExtensionKt.getOfflineCobbleDollars(uuid, getServer()));

//return PlayerExtensionKt.getOfflineCobbleDollars(uuid, getServer());
// return new BigDecimal(getAccount(uuid).getBalance());

// String name = getPlayerName(uuid);


//BigInteger balance = PlayerExtensionKt.getAllPlayerCobbleDollars(getServer()).get(name);
//
//if (balance != null) {
//    LOGGER.info("Found balance: " + balance + " for player: " + name);
//    return new BigDecimal(balance);
//} else {
//    return new BigDecimal(0);
//}

// Class<?> playerClass = Class.forName("net.minecraft.world.entity.player.class_1657");


//public static CobbleDollarsAccount getAccount(UUID uuid) {
//    LOGGER.info("Getting acount for uuid: " + uuid);
//    String playerName = getPlayerName(uuid);
//    LOGGER.info("Got player name: " + playerName);
//    CobbleDollarsAccount account = CobbleDollars.INSTANCE.getCobbleDollarsAccounts().stream()
//        .filter(acc -> acc.getPlayerName().equals(playerName))
//        .findFirst()
//        .orElse(null);
//    LOGGER.info("Found account: " + account);
//    LOGGER.info("All accounts:\n" + CobbleDollars.INSTANCE.getCobbleDollarsAccounts());
//    LOGGER.info("Logger: " + CobbleDollars.INSTANCE.getLOGGER());
//    for (CobbleDollarsAccount acc : CobbleDollars.INSTANCE.getCobbleDollarsAccounts()) {
//        LOGGER.info("Account name: " + acc.getPlayerName());
//    }
//
//    return account;
//}
