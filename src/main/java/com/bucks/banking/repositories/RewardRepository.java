package com.bucks.banking.repositories;

import java.util.List;

import com.bucks.banking.model.Reward;

public interface RewardRepository {
	void addReward(Reward reward);//: Add a reward.
	int getTotalRewardAmount(Long accountNumber);//: Fetch the total rewards for an account.
	List<Reward> getAllRewardsForAccount(Long accountNumber);
}
