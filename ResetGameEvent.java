import java.util.EventObject;

public class ResetGameEvent extends EventObject {
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public ResetGameEvent(Object source) {
        super(source);
    }
}
