package com.mph.services.interfaces;

import java.util.Set;

import com.mph.services.exceptions.RequestAlreadySentException;
import com.mph.services.exceptions.RequestAlreadyReceivedException;

import com.mph.beans.FriendRequest;
import com.mph.beans.User;

public interface FriendRequestService {

	/**
	 * Sends a friend request to an existing user.
	 * 
	 * @param sender the user sending the friend request
	 * @param receiver the user receiving the friend request
	 * 
	 * @throws RequestAlreadySentException if the user sending the request has already sent a friend request to this user
	 * @throws RequestAlreadyReceivedException if the user sending the request has already received a friend request from this user
	 */
	public void sendFriendRequest(User sender, String receiver) throws RequestAlreadySentException, RequestAlreadyReceivedException;

	/**
	 * Cancels a friend request sent.
	 * 
	 * @param friendRequest the friend request to cancel
	 */
	public void cancelFriendRequest(FriendRequest friendRequest);

	/**
	 * Accepts a friend request received.
	 * 
	 * @param friendRequest the friend request to accept
	 */
	public void acceptFriendRequest(FriendRequest friendRequest);

	/**
	 * Declines a friend request received.
	 * 
	 * @param friendRequest the friend request to decline
	 */
	public void declineFriendRequest(FriendRequest friendRequest);

	/**
	 * Cancels a set of friend requests sent.
	 * 
	 * @param friendRequests the friend requests to cancel
	 */
	public void cancelFriendRequests(Set<FriendRequest> friendRequests);

	/**
	 * Declines a set of friend requests received.
	 * 
	 * @param friendRequests the friend requests to decline
	 */
	public void declineFriendRequests(Set<FriendRequest> friendRequests);

	public Set<FriendRequest> getFriendRequestsSentByUser(User user);

	public Set<FriendRequest> getFriendRequestsReceivedByUser(User user);

}