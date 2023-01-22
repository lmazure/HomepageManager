package data;

import java.util.HashSet;
import java.util.Set;

import com.google.common.base.Predicate;

public class ViolationManager {

    final Set<Violation> _set;
    
    public ViolationManager() {
        _set = new HashSet<>();
    }
    
    public void add(final Violation violation) {
        _set.add(violation);
    }
    
    public void remove(final Predicate<Violation> filter) {
        for (final Violation violation: _set) {
            if (filter.apply(violation)) {
                _set.remove(violation);
            }
        }
        return;
    }
}
