/* -*- Mode: Java; tab-width: 4; indent-tabs-mode: t; c-basic-offset: 4 -*- */
/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.work;

import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.List;

/**
 * Created 4/26/21 4:24 AM
 */
public class WorkList {
	private List<WorkJob> jobs = new ArrayList<>();
	private boolean _done;

	public void addJob(final WorkJob aJob) {
		jobs.add(aJob);
	}

	public boolean isDone() {
		return _done;
	}

	public void setDone() {
		_done = true;
	}

	public boolean isEmpty() {
		return jobs.size() == 0;
	}

	public ImmutableList<WorkJob> getJobs() {
		return ImmutableList.copyOf(jobs);
	}
}

//
// vim:set shiftwidth=4 softtabstop=0 noexpandtab://
