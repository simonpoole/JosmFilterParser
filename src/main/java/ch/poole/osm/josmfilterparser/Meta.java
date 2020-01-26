package ch.poole.osm.josmfilterparser;

import java.util.Collection;

import org.jetbrains.annotations.NotNull;

import ch.poole.osm.josmfilterparser.ElementState.State;

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
     * @return the number of ways the Node is a memeber of
     */
    int getWayCount();

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

}
