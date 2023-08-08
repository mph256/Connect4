package com.mph.dao.interfaces;

import java.util.Set;

import com.mph.beans.FriendRequest;
import com.mph.beans.User;

public interface FriendRequestDao {

	public void add(FriendRequest friendRequest);

	public void update(FriendRequest friendRequest);

	public void delete(FriendRequest friendRequest);

	public Set<FriendRequest> findBySender(User user);

	public Set<FriendRequest> findByReceiver(User user);

}