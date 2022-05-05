/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import io.reactivex.rxjava3.subjects.ReplaySubject;
import io.reactivex.rxjava3.subjects.Subject;
import org.jetbrains.annotations.NotNull;
import tripleo.elijah.stages.deduce.FunctionInvocation;
import tripleo.elijah.stages.gen_generic.Dependency;

import java.util.ArrayList;
import java.util.List;


/**
 * Created 6/21/21 11:36 PM
 */
public abstract class AbstractDependencyTracker implements DependencyTracker {
	private List<FunctionInvocation> dependentFunctions = new ArrayList<FunctionInvocation>();
	private List<GenType> dependentTypes = new ArrayList<GenType>();

//	@Override
	public List<GenType> dependentTypes() {
		return dependentTypes;
	}

//	@Override
	public List<FunctionInvocation> dependentFunctions() {
		return dependentFunctions;
	}

	public void addDependentType(@NotNull GenType aType) {
//		dependentTypes.add(aType);
		dependentTypesSubject.onNext(aType);
	}

	public void addDependentFunction(@NotNull FunctionInvocation aFunction) {
//		dependentFunctions.add(aFunction);
		dependentFunctionsSubject.onNext(aFunction);
	}

	Subject<GenType> dependentTypesSubject = ReplaySubject.create(2);/*new Publisher<GenType>() {
		List<Subscriber<GenType>> subscribers = new ArrayList<>(2);

		@Override
		public void subscribe(final Subscriber<? super GenType> aSubscriber) {
			subscribers.add((Subscriber<GenType>) aSubscriber);
		}
	};*/

	public Subject<GenType> dependentTypesSubject() {
		return dependentTypesSubject;
	}

	Subject<FunctionInvocation> dependentFunctionsSubject = ReplaySubject.create(2);/*new Publisher<FunctionInvocation>() {
		List<Subscriber<FunctionInvocation>> subscribers = new ArrayList<>(2);

		@Override
		public void subscribe(final Subscriber<? super FunctionInvocation> aSubscriber) {
			subscribers.add((Subscriber<FunctionInvocation>) aSubscriber);
		}
	};*/

	public Subject<FunctionInvocation> dependentFunctionSubject() {
		return dependentFunctionsSubject;
	}

	public void noteDependencies(final Dependency d) {
		d.noteDependencies(this, dependentFunctions, dependentTypes);
	}
}

//
//
//
