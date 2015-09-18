package com.techlords.crown.business.model.enums;

import java.util.ArrayList;
import java.util.List;

public enum MoveStatusBO {
	MOVED(1), RETURNED(2), ACCEPTED(3), PARTIAL_ACCEPT(4), CANCELLED(5), CLOSED(
			6);

	private final int moveStatusID;

	private MoveStatusBO(final int id) {
		moveStatusID = id;
	}

	public static final MoveStatusBO valueOf(int id) {
		for (MoveStatusBO ms : values()) {
			if (ms.getMoveStatusID() == id) {
				return ms;
			}
		}
		return null;
	}

	public static final List<MoveStatusBO> getValues(List<Integer> ids) {
		final List<MoveStatusBO> statuses = new ArrayList<MoveStatusBO>();
		for (final int id : ids) {
			MoveStatusBO bo = valueOf(id);
			if (bo != null) {
				statuses.add(bo);
			}
		}
		return statuses;
	}

	public final String getMoveStatus() {
		return name();
	}

	public final int getMoveStatusID() {
		return moveStatusID;
	}
}