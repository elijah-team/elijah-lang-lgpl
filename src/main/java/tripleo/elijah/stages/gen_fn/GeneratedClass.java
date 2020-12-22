/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import tripleo.elijah.lang.*;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 10/29/20 4:26 AM
 */
public class GeneratedClass implements GeneratedNode {
	private final OS_Module module;
	private final ClassStatement klass;
	public List<VarTableEntry> varTable = new ArrayList<VarTableEntry>();
	protected Map<FunctionDef, GeneratedFunction> functionMap = new HashMap<FunctionDef, GeneratedFunction>();

	public GeneratedClass(ClassStatement klass, OS_Module module) {
		this.klass = klass;
		this.module = module;
	}

	public void addVarTableEntry(AccessNotation an, VariableStatement vs) {
		// TODO dont ignore AccessNotation
		varTable.add(new VarTableEntry(vs.getNameToken(), vs.initialValue()));
	}

	public void addAccessNotation(AccessNotation an) {
		throw new NotImplementedException();
	}

	public void createCtor0() {
		// TODO implement me
		FunctionDef fd = new FunctionDef(klass, klass.getContext());
		for (VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.initialValue != IExpression.UNASSIGNED) {
				IExpression left = varTableEntry.nameToken;
				IExpression right = varTableEntry.initialValue;

				IExpression e = ExpressionBuilder.build(left, ExpressionKind.ASSIGNMENT, right);
				fd.add(new StatementWrapper(e, fd.getContext(), fd));
			} else {
				if (getPragma("auto_construct")) {
					fd.add(new ConstructExpression(fd, fd.getContext(), varTableEntry.nameToken, null));
				}
			}
		}
	}

	private boolean getPragma(String auto_construct) { // TODO this should be part of Context
		return false;
	}

	public String getName() {
		return klass.getName();
	}

	public void addFunction(FunctionDef functionDef, GeneratedFunction generatedFunction) {
		if (functionMap.containsKey(functionDef))
			throw new IllegalStateException("Function already generated"); // TODO do better than this
		functionMap.put(functionDef, generatedFunction);
	}

	/**
	 * Get a {@link GeneratedFunction}
	 *
	 * @param fd the function searching for
	 *
	 * @return null if no such key exists
	 */
	public GeneratedFunction getFunction(FunctionDef fd) {
		return functionMap.get(fd);
	}

	public ClassStatement getKlass() {
		return this.klass;
	}

	public class VarTableEntry {
		public final IdentExpression nameToken;
		public final IExpression initialValue;
		public OS_Type varType;

		public VarTableEntry(IdentExpression nameToken, IExpression initialValue) {
			this.nameToken = nameToken;
			this.initialValue = initialValue;
		}
	}
}

//
//
//
