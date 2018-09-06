package com.cn.thinkx.pms.model.card;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

public class composeCardAccountIdPK implements Serializable {
	private TbCardInf card;
	private TbAccountInf account;

	 @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	 @JoinColumn(name = "CARD_NO")
	public TbCardInf getCard() {
		return card;
	}

	public void setCard(TbCardInf card) {
		this.card = card;
	}

	 @ManyToOne(cascade=CascadeType.ALL,fetch=FetchType.LAZY)
	 @JoinColumn(name = "ACCOUNT_NO")
	public TbAccountInf getAccount() {
		return account;
	}

	public void setAccount(TbAccountInf account) {
		this.account = account;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj instanceof composeCardAccountIdPK) {
			composeCardAccountIdPK pk = (composeCardAccountIdPK) obj;
			if (this.card.equals(pk.card) && this.account.equals(pk.account)) {
				return true;
			}
		}
		return false;
	}

	@Override
	public int hashCode() {
		return super.hashCode();
	}
}
