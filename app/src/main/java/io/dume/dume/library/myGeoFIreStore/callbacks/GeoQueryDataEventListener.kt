package io.dume.dume.library.myGeoFIreStore.callbacks

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.GeoPoint

// FULLY TESTED

/**
 * GeoQuery notifies listeners with this interface about documents that entered, exited, or moved within the query.
 */
interface GeoQueryDataEventListener {

    /**
     * Called if a document entered the search area of the GeoQuery. This method is called for every document currently in the
     * search area at the time of adding the listener.
     *
     * This method is once per document, and is only called again if onDocumentExited was called in the meantime.
     *
     * @param documentSnapshot The snapshot of the associated document that entered the search area
     * @param location The location for this document
     */
    fun onDocumentEntered(documentSnapshot: DocumentSnapshot, location: GeoPoint)

    /**
     * Called if a document exited the search area of the GeoQuery. This is method is only called if onDocumentEntered was called
     * for the document.
     *
     * @param documentSnapshot The snapshot of the associated document that exited the search area
     */
    fun onDocumentExited(documentSnapshot: DocumentSnapshot)

    /**
     * Called if a document moved within the search area.
     *
     * This method can be called multiple times.
     *
     * @param documentSnapshot The snapshot of the associated document that moved within the search area
     * @param location The location for this document
     */
    fun onDocumentMoved(documentSnapshot: DocumentSnapshot, location: GeoPoint)

    /**
     * Called if a document changed within the search area.
     *
     * An onDocumentMoved() is always followed by onDocumentChanged() but it is be possible to see
     * onDocumentChanged() without an preceding onDocumentMoved().
     *
     * This method can be called multiple times for a single location change, due to the way
     * Firestore handles floating point numbers.
     *
     *
     * @param documentSnapshot The snapshot of the associated document that moved within the search area
     * @param location The location for this document
     */
    fun onDocumentChanged(documentSnapshot: DocumentSnapshot, location: GeoPoint)

    /**
     * Called once all initial GeoFirestore data has been loaded and the relevant events have been fired for this query.
     * Every time the query criteria is updated, this observer will be called after the updated query has fired the
     * appropriate document entered or document exited events.
     */
    fun onGeoQueryReady()

    /**
     * Called in case an exception occurred while retrieving locations for a query, e.g. violating security rules.
     * @param exception The exception that occurred while retrieving the query
     */
    fun onGeoQueryError(exception: Exception)

}
