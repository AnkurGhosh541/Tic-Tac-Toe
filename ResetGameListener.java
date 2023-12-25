import java.util.EventListener;

public interface ResetGameListener extends EventListener {
    void resetGameRequested(ResetGameEvent event);
}
