package hu.psprog.leaflet.bridge.service;

import hu.psprog.leaflet.api.rest.request.user.UpdateProfileRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UpdateRoleRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserCreateRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserInitializeRequestModel;
import hu.psprog.leaflet.api.rest.request.user.UserPasswordRequestModel;
import hu.psprog.leaflet.api.rest.response.user.ExtendedUserDataModel;
import hu.psprog.leaflet.api.rest.response.user.UserListDataModel;
import hu.psprog.leaflet.bridge.client.exception.CommunicationFailureException;

/**
 * BridgeClient interface for user related calls.
 *
 * @author Peter Smith
 */
public interface UserBridgeService {

    /**
     * Returns basic information of all existing users.
     *
     * @return response mapped to {@link UserListDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    UserListDataModel getAllUsers() throws CommunicationFailureException;

    /**
     * Creates a new user. Can be uses only after user database is initialized.
     *
     * @param userCreateRequestModel user data
     * @return response mapped to {@link ExtendedUserDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    ExtendedUserDataModel createUser(UserCreateRequestModel userCreateRequestModel) throws CommunicationFailureException;

    /**
     * Initializes user database with the first user who always has ADMIN role. After initialization, endpoint shall not be used!
     *
     * @param userInitializeRequestModel user data
     * @return response mapped to {@link ExtendedUserDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    ExtendedUserDataModel initUserDatabase(UserInitializeRequestModel userInitializeRequestModel) throws CommunicationFailureException;

    /**
     * Returns user identified by given ID.
     *
     * @param userID requested user's ID
     * @return response mapped to {@link ExtendedUserDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    ExtendedUserDataModel getUserByID(Long userID) throws CommunicationFailureException;

    /**
     * Deletes given user.
     *
     * @param userID requested user's ID
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    void deleteUser(Long userID) throws CommunicationFailureException;

    /**
     * Updates given user's role.
     *
     * @param userID requested user's ID
     * @param updateRoleRequestModel user data
     * @return response mapped to {@link ExtendedUserDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    ExtendedUserDataModel updateRole(Long userID, UpdateRoleRequestModel updateRoleRequestModel) throws CommunicationFailureException;

    /**
     * Updates given user's changeable profile fields.
     *
     * @param userID requested user's ID
     * @param updateProfileRequestModel user data
     * @return response mapped to {@link ExtendedUserDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    ExtendedUserDataModel updateProfile(Long userID, UpdateProfileRequestModel updateProfileRequestModel) throws CommunicationFailureException;

    /**
     * Updates given user's password.
     *
     * @param userID requested user's ID
     * @param userPasswordRequestModel user data
     * @return response mapped to {@link ExtendedUserDataModel}
     * @throws CommunicationFailureException if client fails to reach backend application
     */
    ExtendedUserDataModel updatePassword(Long userID, UserPasswordRequestModel userPasswordRequestModel) throws CommunicationFailureException;
}
