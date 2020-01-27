package ch.poole.osm.josmfilterparser;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.jetbrains.annotations.NotNull;

import ch.poole.osm.josmfilterparser.ElementState.State;

/**
 * Dummy Meta implementation for testing
 * 
 * @author simon
 *
 */
public class TestMeta implements Meta {

    long         id;
    long         version;
    long         changeset;
    long         timestamp;
    State        state;
    boolean      isClosed;
    int          nodeCount;
    int          wayCount;
    int          areaSize;
    int          wayLength;
    List<String> roles = new ArrayList<>();
    String       user;
    boolean      selected;
    String       hasRole;
    String       preset;

    @Override
    public String getUser() {
        return user;
    }

    @Override
    public long getId() {
        return id;
    }

    @Override
    public long getVersion() {
        return version;
    }

    @Override
    public long getChangeset() {
        return changeset;
    }

    @Override
    public long getTimestamp() {
        return timestamp;
    }

    @Override
    public State getState() {
        return state;
    }

    @Override
    public boolean isClosed() {
        return isClosed;
    }

    @Override
    public int getNodeCount() {
        return nodeCount;
    }

    @Override
    public int getWayCount() {
        return wayCount;
    }

    @Override
    public int getAreaSize() {
        return areaSize;
    }

    @Override
    public int getWayLength() {
        return wayLength;
    }

    @Override
    public @NotNull Collection<String> getRoles() {
        return roles;
    }

    @Override
    public boolean isSelected() {
        return selected;
    }

    @Override
    public boolean hasRole(@NotNull String role) {
        return role.equals(hasRole);
    }

    @Override
    public boolean matchesPreset(@NotNull String preset) {
        return preset.equals(this.preset);
    }
}
