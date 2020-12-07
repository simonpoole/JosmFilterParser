package ch.poole.osm.josmfilterparser;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import ch.poole.osm.josmfilterparser.ElementState.State;

/**
 * Interface to a real OSM object
 * 
 * @author simon
 *
 */
public interface Meta {

    /**
     * Get the OSM display name
     * 
     * @return the OSM display name
     */
    String getUser();

    /**
     * Get the OSM id of the object
     * 
     * @return the OSM id of the object
     */
    long getId();

    /**
     * Get the version of the object
     * 
     * @return the version of the object
     */
    long getVersion();

    /**
     * Get the changeset id for this object
     * 
     * @return the changeset id for this object
     */
    long getChangeset();

    /**
     * Get the timestamp (when this version of the object was created)
     * 
     * @return the timestamp in seconds since the Unic EPOCH
     */
    long getTimestamp();

    /**
     * Get the state of the object
     * 
     * @return a State
     */
    @NotNull
    State getState();

    /**
     * If the object is a Way check if it is closed
     * 
     * @return true if the way is closed
     */
    boolean isClosed();

    /**
     * If the object is a Way return the number of way nodes
     * 
     * @return the number of way nodes
     */
    int getNodeCount();

    /**
     * If the object is a Node return the number of ways it is a member of
     * 
     * @return the number of ways the Node is a member of
     */
    int getWayCount();

    /**
     * If the object is a Relation return the number of members it has
     * 
     * If not implemented this returns -1 which should always evaluate to false
     * 
     * @return the number of members the Relation has
     */
    default int getMemberCount() {
        return Range.UNINITALIZED;
    }

    /**
     * If the object is a Way and closed return the area it covers
     * 
     * @return the area it covers in m^2
     */
    int getAreaSize();

    /**
     * If the object is a Way return its length
     * 
     * @return the the length in m
     */
    int getWayLength();

    /**
     * Get any roles the element has in Relations
     * 
     * @return a Collection containing the roles
     */
    @NotNull
    Collection<String> getRoles();

    /**
     * Check if the element is selected
     * 
     * @return
     */
    boolean isSelected();

    /**
     * Check if a relation has a member with role
     * 
     * @param role the role
     * @return true if there is a member with the role
     */
    boolean hasRole(@NotNull String role);

    /**
     * Get a preset from a path specification
     * 
     * @param presetPath the path
     * @return an Object that should be a instance of a preset for the syste,
     */
    @Nullable
    Object getPreset(@NotNull String presetPath);

    /**
     * Check if the object matches with a preset or a preset group
     * 
     * @param preset the path to the preset or group
     */
    boolean matchesPreset(@NotNull Object preset);

    /**
     * Check if the element is incomplete (this is not defined in the documentation)
     * 
     * @return true if incomplete
     */
    boolean isIncomplete();

    /**
     * Check if the element is in the current view
     * 
     * @return true if in view
     */
    boolean isInview();

    /**
     * Check if the element and all member elements is in the current view
     * 
     * @return true if in view
     */
    boolean isAllInview();

    /**
     * Check if the element is in the downloaded areas
     * 
     * @return true if in view
     */
    boolean isInDownloadedArea();

    /**
     * Check if the element and all member elements is in the downloaded areas
     * 
     * @return true if in view
     */
    boolean isAllInDownloadedArea();
}
