/* GNU Prolog for Java
 * Copyright (C) 1997-1999  Constantine Plotnikov
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Library General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Library General Public License for more details.
 *
 * You should have received a copy of the GNU Library General Public
 * License along with this library; if not, write to the
 * Free Software Foundation, Inc., 59 Temple Place - Suite 330,
 * Boston, MA  02111-1307, USA. The text of license can be also found
 * at http://www.gnu.org/copyleft/lgpl.html
 */
package gnu.prolog.vm.interpreter;

import gnu.prolog.term.Term;
import gnu.prolog.vm.BacktrackInfo;

/**
 * this backtrack info is put to stack upon exit from from user defined
 * predicate in return instruction. As result it is BacktrackInfo that is passed
 * to user defined predicate as parameter, no other backtrack info will be
 * passed as parameter to user defined predicate on redo.
 */
public class LeaveByteCodeBacktrackInfo extends BacktrackInfo
{
	/** execution state */
	public BacktrackInfo startBacktrackInfo;
	public Term environment[];

	/**
	 * a constructor
	 * 
	 * @param environment
	 * @param startBacktrackInfo
	 */
	public LeaveByteCodeBacktrackInfo(Term environment[], BacktrackInfo startBacktrackInfo)
	{
		super(-1, -1);
		this.environment = environment;
		this.startBacktrackInfo = startBacktrackInfo;
	}
}
