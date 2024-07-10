package net.azureaaron.hmapi.network.packet.c2s;

import net.azureaaron.hmapi.network.packet.HypixelPacket;

public sealed interface HypixelC2SPacket extends HypixelPacket permits PartyInfoC2SPacket, PlayerInfoC2SPacket, RegisterC2SPacket {

}
