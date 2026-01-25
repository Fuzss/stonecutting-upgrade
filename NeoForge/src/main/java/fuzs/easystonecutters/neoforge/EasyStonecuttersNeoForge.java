package fuzs.easystonecutters.neoforge;

import fuzs.easystonecutters.EasyStonecutters;
import fuzs.puzzleslib.api.core.v1.ModConstructor;
import net.neoforged.fml.common.Mod;

@Mod(EasyStonecutters.MOD_ID)
public class EasyStonecuttersNeoForge {

    public EasyStonecuttersNeoForge() {
        ModConstructor.construct(EasyStonecutters.MOD_ID, EasyStonecutters::new);
    }
}
