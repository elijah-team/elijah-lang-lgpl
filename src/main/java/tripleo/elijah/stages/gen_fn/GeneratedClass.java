/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.gen_fn;

import org.jetbrains.annotations.Nullable;
import tripleo.elijah.lang.*;
import tripleo.elijah.stages.deduce.DeduceLookupUtils;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created 10/29/20 4:26 AM
 */
public class GeneratedClass implements GeneratedContainer {
	private final OS_Module module;
	private final ClassStatement klass;
	public List<VarTableEntry> varTable = new ArrayList<VarTableEntry>();
	public Map<FunctionDef, GeneratedFunction> functionMap = new HashMap<FunctionDef, GeneratedFunction>();
	public Map<ConstructorDef, GeneratedFunction> constructors = new HashMap<ConstructorDef, GeneratedFunction>();

	public GeneratedClass(ClassStatement klass, OS_Module module) {
		this.klass = klass;
		this.module = module;
	}

	public void addVarTableEntry(AccessNotation an, VariableStatement vs) {
		// TODO dont ignore AccessNotation
		varTable.add(new VarTableEntry(vs.getNameToken(), vs.initialValue(), vs.typeName()));
	}

	public void addAccessNotation(AccessNotation an) {
		throw new NotImplementedException();
	}

	public void createCtor0() {
		// TODO implement me
		FunctionDef fd = new FunctionDef(klass, klass.getContext());
		fd.setName(Helpers.string_to_ident("<ctor$0>"));
		Scope3 scope3 = new Scope3(fd);
		fd.scope(scope3);
		for (VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.initialValue != IExpression.UNASSIGNED) {
				IExpression left = varTableEntry.nameToken;
				IExpression right = varTableEntry.initialValue;

				IExpression e = ExpressionBuilder.build(left, ExpressionKind.ASSIGNMENT, right);
				scope3.add(new StatementWrapper(e, fd.getContext(), fd));
			} else {
				if (getPragma("auto_construct")) {
					scope3.add(new ConstructStatement(fd, fd.getContext(), varTableEntry.nameToken, null));
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

	public void addConstructor(ConstructorDef aConstructorDef, GeneratedFunction aGeneratedFunction) {
		constructors.put(aConstructorDef, aGeneratedFunction);
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

    @Override
    public String identityString() {
        return ""+klass;
    }

    @Override
    public OS_Module module() {
        return module;
    }

	public void resolve_var_table_entries() {
		for (VarTableEntry varTableEntry : varTable) {
			int y=2;
			if (varTableEntry.potentialTypes.size() == 0 && varTableEntry.varType == null) {
				final TypeName tn = varTableEntry.typeName;
				if (tn != null) {
					if (tn instanceof NormalTypeName) {
						final NormalTypeName tn2 = (NormalTypeName) tn;
						LookupResultList lrl = tn.getContext().lookup(tn2.getName());
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							if (best instanceof AliasStatement)
								best = DeduceLookupUtils._resolveAlias((AliasStatement) best);
							assert best instanceof ClassStatement;
							varTableEntry.varType = new OS_Type((ClassStatement) best);
						} else {
							// TODO shouldn't this already be calculated?
						}
					}
				} else {
					// must be unknown
				}
			} else {

			}
		}
	}

	@Override
	public OS_Element getElement() {
		return getKlass();
	}

	@Override
	@Nullable public VarTableEntry getVariable(String aVarName) {
		for (VarTableEntry varTableEntry : varTable) {
			if (varTableEntry.nameToken.getText().equals(aVarName))
				return varTableEntry;
		}
		return null;
	}
}

//
//
//
