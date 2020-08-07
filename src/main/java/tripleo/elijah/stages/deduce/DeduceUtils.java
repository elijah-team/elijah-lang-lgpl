package tripleo.elijah.stages.deduce;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import org.checkerframework.checker.nullness.qual.Nullable;
import tripleo.elijah.lang.*;

import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * Created 8/3/20 8:41 AM
 */
public class DeduceUtils {
	static class MatchArgs implements Predicate {

		private final ExpressionList args;

		public MatchArgs(ExpressionList args) {
			this.args = args;
		}

		@Override
		public boolean test(@Nullable Object input) {
			if (args == null && ((ConstructorDef)input).fal().falis.size() == 0)
				return true;
			else
				return false;
		}
	}

	static class IsConstructor implements com.google.common.base.Predicate<OS_Element> {
		@Override
		public boolean apply(@Nullable OS_Element input) {
			return input instanceof ConstructorDef;
		}
	}

	static class MatchConstructorArgs implements java.util.function.Predicate {
		private final ProcedureCallExpression pce;

		public MatchConstructorArgs(ProcedureCallExpression pce) {
			this.pce = pce;
		}

		@Override
		public boolean test(Object o) {
			final ExpressionList args = pce.getArgs();
			// See if candidate matches args
			if (((LookupResult)o).getElement() instanceof ClassStatement) {
				//o filter isCtor each (each args isCompat)
				ClassStatement klass = (ClassStatement) ((LookupResult)o).getElement();

				Iterable<ClassItem> ctors = Iterables.filter(klass.getItems(), new IsConstructor());
				Iterable<ClassItem> ctors2 = Iterables.filter(ctors, new MatchFunctionArgs(pce));
//				return ctors.iterator().hasNext();
				return Lists.newArrayList(ctors2).size() > 0;

//				return true; // TODO
			}
			System.out.println(o);
			return false;
		}
	}
	static class MatchFunctionArgs implements com.google.common.base.Predicate<ClassItem> {
		private final ProcedureCallExpression pce;

		public MatchFunctionArgs(ProcedureCallExpression pce) {
			this.pce = pce;
		}

		@Override
		public boolean apply(ClassItem o) {
			//  TODO what about __call__ and __ctor__ for ClassStatement?
//			System.out.println("2000 "+o);
			if (!(o instanceof FunctionDef)) return false;
			//
			final ExpressionList args = pce.getArgs();
			// See if candidate matches args
			/*if (((LookupResult)o).getElement() instanceof FunctionDef)*/ {
				//o filter isCtor each (each args isCompat)
				FunctionDef fd = (FunctionDef) (/*(LookupResult)*/o)/*.getElement()*/;
				List<OS_Element2> matching_functions = (List<OS_Element2>) fd.items()
						                                       .stream()
						                                       .filter(new MatchArgs(pce.getArgs()))
						                                       .collect(Collectors.toList());
				return matching_functions.size() > 0;
			}
//			return false;
		}
	}
}

//
//
//
