package fuzs.stonecuttingupgrade.config;

import net.minecraft.client.input.KeyEvent;

import java.util.function.Predicate;

public enum MoveAllItems implements Predicate<KeyEvent> {
    NEVER {
        @Override
        public boolean test(KeyEvent keyEvent) {
            return false;
        }
    },
    HOLDING_SHIFT {
        @Override
        public boolean test(KeyEvent keyEvent) {
            return keyEvent.hasShiftDown();
        }
    },
    ALWAYS {
        @Override
        public boolean test(KeyEvent keyEvent) {
            return true;
        }
    };
}
