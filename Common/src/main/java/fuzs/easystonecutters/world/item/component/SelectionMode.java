package fuzs.easystonecutters.world.item.component;

import fuzs.easystonecutters.init.ModRegistry;
import io.netty.buffer.ByteBuf;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.StringRepresentable;

import java.util.Locale;
import java.util.function.IntFunction;

public enum SelectionMode implements StringRepresentable {
    FLAT,
    CUBE,
    LINE;

    private static final SelectionMode[] VALUES = SelectionMode.values();
    public static final StringRepresentable.StringRepresentableCodec<SelectionMode> CODEC = StringRepresentable.fromEnum(
            SelectionMode::values);
    public static final IntFunction<SelectionMode> BY_ID = ByIdMap.continuous(SelectionMode::ordinal,
            values(),
            ByIdMap.OutOfBoundsStrategy.WRAP);
    public static final StreamCodec<ByteBuf, SelectionMode> STREAM_CODEC = ByteBufCodecs.idMapper(BY_ID,
            SelectionMode::ordinal);

    public Component getComponent() {
        String translationKey =
                ModRegistry.POCKET_STONECUTTER_ITEM.value().getDescriptionId() + "." + this.getSerializedName();
        return Component.translatable(translationKey).withStyle(ChatFormatting.GOLD);
    }

    public SelectionMode cycle() {
        return BY_ID.apply(this.ordinal() + 1);
    }

    @Override
    public String getSerializedName() {
        return this.name().toLowerCase(Locale.ROOT);
    }
}
