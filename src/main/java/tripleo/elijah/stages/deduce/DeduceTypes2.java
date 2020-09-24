/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import org.jetbrains.annotations.NotNull;
import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.Helpers;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Stack;

/**
 * Created 9/15/20 12:51 PM
 */
public class DeduceTypes2 {
	private final OS_Module module;

	public DeduceTypes2(OS_Module m) {
		module = m;
	}

	public void deduceFunctions(List<GeneratedFunction> lgf) {
		for (GeneratedFunction generatedFunction : lgf) {
			deduce_generated_function(generatedFunction);
		}
	}

	public void deduce_generated_function(GeneratedFunction generatedFunction) {
		OS_Element fd = generatedFunction.getFD();
		Context fd_ctx = fd.getContext();
		//
		for (Instruction instruction : generatedFunction.instructions()) {
			final Context context = generatedFunction.getContextFromPC(instruction.getIndex());
			System.out.println("8006 " + instruction);
			switch (instruction.getName()) {
			case E: {
				//
				// resolve all cte expressions
				//
				for (ConstantTableEntry cte : generatedFunction.cte_list) {
					IExpression iv = cte.initialValue;
					switch (iv.getKind()) {
					case NUMERIC:
						OS_Type a = cte.getTypeTableEntry().attached;
						if (a == null || a.getType() != OS_Type.Type.USER_CLASS) {
							cte.getTypeTableEntry().attached = resolve_type(new OS_Type(BuiltInTypes.SystemInteger), context);
						}
						break;
					default:
						throw new NotImplementedException();
					}
				}
			}
				break;
			case X:
				{
					// TODO brittle: is alias points to alias, will fail
					for (VariableTableEntry vte : generatedFunction.vte_list) {
//						System.out.println("704 "+vte.type.attached+" "+vte.potentialTypes());
						int y=2;
						if (vte.type.attached != null) {
							TypeName x = vte.type.attached.getTypeName();
							if (x instanceof NormalTypeName) {
								String tn = ((NormalTypeName) x).getName();
								LookupResultList lrl = x.getContext().lookup(tn);
								OS_Element best = lrl.chooseBest(null);
								while (best instanceof AliasStatement) {
									best = _resolveAlias((AliasStatement) best); // TODO write _resolveAliasFully
								}
								if (!(OS_Type.isConcreteType(best))) {
									module.parent.eee.reportError("Not a concrete type "+best);
								} else {
//								System.out.println("705 " + best);
									vte.type.attached = new OS_Type((ClassStatement) best);
								}
							}
						}
					}
				}
				break;
			case ES:
				break;
			case XS:
				break;
			case AGN:
				{
					final IntegerIA arg = (IntegerIA)instruction.getArg(0);
					VariableTableEntry vte = generatedFunction.getVarTableEntry(arg.getIndex());
					InstructionArgument i2 = instruction.getArg(1);
					if (i2 instanceof IntegerIA) {
						VariableTableEntry vte2 = generatedFunction.getVarTableEntry(((IntegerIA) i2).getIndex());
						vte.addPotentialType(instruction.getIndex(), vte2.type);
//							throw new NotImplementedException();
					} else if (i2 instanceof FnCallArgs) {
						FnCallArgs fca = (FnCallArgs) i2;
						do_assign_call(generatedFunction, fd_ctx, vte, fca, instruction.getIndex());
					} else if (i2 instanceof ConstTableIA) {
						int y=2;
						do_assign_constant(generatedFunction, instruction, vte, (ConstTableIA) i2);
					} else
						throw new NotImplementedException();
				}
				break;
			case AGNK:
				{
					final IntegerIA arg = (IntegerIA)instruction.getArg(0);
					VariableTableEntry vte = generatedFunction.getVarTableEntry(arg.getIndex());
					InstructionArgument i2 = instruction.getArg(1);
					ConstTableIA ctia = (ConstTableIA) i2;
					do_assign_constant(generatedFunction, instruction, vte, ctia);
				}
				break;
			case AGNT:
				break;
			case AGNF:
				break;
			case CMP:
				break;
			case JE:
				break;
			case JL:
				break;
			case JMP:
				break;
			case CALL: {
				int i1 = to_int(instruction.getArg(0));
				InstructionArgument i2 = (instruction.getArg(1));
				ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
					IExpression pn1 = fn1.expression;
					if (pn1 instanceof IdentExpression) {
						String pn = ((IdentExpression) pn1).getText();
						LookupResultList lrl = fd_ctx.lookup(pn);
						OS_Element best = lrl.chooseBest(null);
						if (best != null) {
							fn1.resolved = best; // TODO check arity and arg matching
						} else
							throw new NotImplementedException();
					} else
						throw new NotImplementedException();

				}
				if (i2 instanceof IntegerIA) {
					int i2i = to_int(i2);
					VariableTableEntry vte = generatedFunction.getVarTableEntry(i2i);
					int y =2;
				} else
					throw new NotImplementedException();
			}
			break;
			case CALLS: {
				int i1 = to_int(instruction.getArg(0));
				InstructionArgument i2 = (instruction.getArg(1));
				ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
					implement_calls(generatedFunction, fd_ctx, i2, fn1, instruction.getIndex());
				}
/*
				if (i2 instanceof IntegerIA) {
					int i2i = to_int(i2);
					VariableTableEntry vte = generatedFunction.getVarTableEntry(i2i);
					int y =2;
				} else
					throw new NotImplementedException();
*/
			}
			break;
			case RET:
				break;
			case YIELD:
				break;
			case TRY:
				break;
			case PC:
				break;
			case DOT:
				break;
			}
		}
		for (VariableTableEntry vte : generatedFunction.vte_list) {
			if (vte.type.attached == null)
				if (vte.potentialTypes().size() == 1)
					vte.type.attached = new ArrayList<TypeTableEntry>(vte.potentialTypes()).get(0).attached;
				else
					System.err.println("703 "+vte.getName()+" "+vte.potentialTypes());
		}
		{
			//
			// NOW CALCULATE DEFERRED CALLS
			//
			for (Integer deferred_call : generatedFunction.deferred_calls) {
				final Instruction instruction = generatedFunction.getInstruction(deferred_call);

				int i1 = to_int(instruction.getArg(0));
				InstructionArgument i2 = (instruction.getArg(1));
				ProcTableEntry fn1 = generatedFunction.getProcTableEntry(i1);
				{
//					generatedFunction.deferred_calls.remove(deferred_call);
					implement_calls_(generatedFunction, fd_ctx, i2, fn1, instruction.getIndex());
				}
			}
		}
	}

	private OS_Type resolve_type(OS_Type type, Context ctx) {
		switch (type.getType()) {

		case BUILT_IN:
			{
				switch (type.getBType()) {
				case SystemInteger:
					{
						LookupResultList lrl = module.prelude.getContext().lookup("SystemInteger");
						OS_Element best = lrl.chooseBest(null);
						while (!(best instanceof ClassStatement)) {
							if (best instanceof AliasStatement) {
								best = _resolveAlias((AliasStatement) best);
							}
						}
						return new OS_Type((ClassStatement) best);
					}
				case Boolean:
					{
						LookupResultList lrl = module.prelude.getContext().lookup("Boolean");
						OS_Element best = lrl.chooseBest(null);
						return new OS_Type((ClassStatement) best); // TODO might change to Type
					}
				}
			}
			break;
		case USER:
			{
				TypeName tn1 = type.getTypeName();
				if (tn1 instanceof NormalTypeName) {
					String tn = ((NormalTypeName) tn1).getName();
					System.out.println("799 "+tn);
					LookupResultList lrl = tn1.getContext().lookup(tn); // TODO is this right?
					OS_Element best = lrl.chooseBest(null);
					return new OS_Type((ClassStatement) best); // TODO might change to Type
				}
				throw new NotImplementedException(); // TODO might be Qualident, etc
			}
//			break;
		case USER_CLASS:
			return type;
		case FUNCTION:
			return type;
		}
		throw new IllegalStateException("Cant be here.");
	}

	private void do_assign_constant(GeneratedFunction generatedFunction, Instruction instruction, VariableTableEntry vte, ConstTableIA i2) {
		if (vte.type.attached != null) {
			// TODO check types
		}
		ConstantTableEntry cte = generatedFunction.getConstTableEntry(i2.getIndex());
		if (cte.type.attached == null) {
			System.out.println("Null type in CTE "+cte);
		}
//							vte.type = cte.type;
		vte.addPotentialType(instruction.getIndex(), cte.type);
	}

	private void do_assign_call(GeneratedFunction generatedFunction, Context ctx, VariableTableEntry vte, FnCallArgs fca, int instructionIndex) {
		ProcTableEntry pte = generatedFunction.getProcTableEntry(to_int(fca.getArg(0)));
		for (TypeTableEntry tte : pte.getArgs()) { // TODO this looks wrong
			System.out.println("770 "+tte);
			IExpression e = tte.expression;
			if (e == null) continue;
			switch (e.getKind()) {
			case NUMERIC:
				{
					tte.attached = new OS_Type(BuiltInTypes.SystemInteger);
					vte.type = tte;
				}
				break;
			case IDENT:
				{
/*
					LookupResultList lrl = ctx.lookup(((IdentExpression)e).getText());
					OS_Element best = lrl.chooseBest(null);
					int y=2;
*/
					InstructionArgument yy = generatedFunction.vte_lookup(((IdentExpression) e).getText());
//					System.out.println("10000 "+yy);
					Collection<TypeTableEntry> c = generatedFunction.getVarTableEntry(to_int(yy)).potentialTypes();
					List<TypeTableEntry> ll = new ArrayList<>(c);
					if (ll.size() == 1) {
						tte.attached = ll.get(0).attached;
						vte.addPotentialType(instructionIndex, ll.get(0));
					} else
						throw new NotImplementedException();
				}
				break;
			default:
				{
					throw new NotImplementedException();
				}
			}
		}
		{
			LookupResultList lrl = ctx.lookup(((IdentExpression)pte.expression).getText());
			OS_Element best = lrl.chooseBest(null);
			if (best != null)
				pte.resolved = best; // TODO do we need to add a dependency for class?
			else
				throw new NotImplementedException();
		}
	}

	private void implement_calls(GeneratedFunction gf, Context context, InstructionArgument i2, ProcTableEntry fn1, int pc) {
		if (gf.deferred_calls.contains(pc)) {
			System.err.println("Call is deferred "/*+gf.getInstruction(pc)*/+" "+fn1);
			return;
		}
		implement_calls_(gf, context, i2, fn1, pc);
	}

	private void implement_calls_(GeneratedFunction gf, Context context, InstructionArgument i2, ProcTableEntry fn1, int pc) {
		IExpression pn1 = fn1.expression;
		if (pn1 instanceof IdentExpression) {
			String pn = ((IdentExpression) pn1).getText();
			boolean found = lookup_name_calls(context, pn, fn1);
			LookupResultList lrl;
			OS_Element best;
			if (found) return;

			String pn2 = reverse_name(pn);
//			System.out.println("7002 "+pn2);
			found = lookup_name_calls(context, pn2, fn1);
			if (found) return;

			final VariableTableEntry vte = gf.getVarTableEntry(to_int(i2));
			final Context ctx = gf.getContextFromPC(pc);
			LookupResultList lrl2 = ctx.lookup(vte.getName());
//			System.out.println("7003 "+vte.getName()+" "+ctx);
			OS_Element best2 = lrl2.chooseBest(null);
			if (best2 != null) {
				found = lookup_name_calls(best2.getContext(), pn, fn1);
				if (found) return;

				found = lookup_name_calls(best2.getContext(), pn2, fn1);
				if (!found) {
					//throw new NotImplementedException(); // TODO
					module.parent.eee.reportError("Special Function not found " + pn);
				}

			} else {
				throw new NotImplementedException(); // Cant find vte, should never happen
			}


		} else
			throw new NotImplementedException(); // pn1 is not IdentExpression
	}

	private boolean lookup_name_calls(Context ctx, String pn, ProcTableEntry fn1) {
		LookupResultList lrl = ctx.lookup(pn);
		OS_Element best = lrl.chooseBest(null);
		if (best != null) {
			fn1.resolved = best; // TODO check arity and arg matching
			return true;
		}
		return false;
	}

	private String reverse_name(String pn) {
		byte[] pnc = pn.getBytes(); // TODO warning ascii
		byte[] pn2c = new byte[pnc.length+2];
		System.arraycopy(pnc, 0, pn2c, 0, 2);
		pn2c[2] = 'r';
		System.arraycopy(pnc, 2, pn2c, 3, pnc.length-2);
		pn2c[pn2c.length-1] = '\0';
		return new String(pn2c);
	}

	public static int to_int(InstructionArgument arg) {
		return ((IntegerIA) arg).getIndex();
	}

	private OS_Element _resolveAlias(AliasStatement aliasStatement) {
		LookupResultList lrl2;
		if (aliasStatement.getExpression() instanceof Qualident) {
			IExpression de = Helpers.qualidentToDotExpression2(((Qualident) aliasStatement.getExpression()));
			if (de instanceof DotExpression)
				lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			else
				lrl2 = aliasStatement.getContext().lookup(((IdentExpression) de).getText());
			return lrl2.chooseBest(null);
		}
		// TODO what about when DotExpression is not just simple x.y.z? then alias equivalent to val
		if (aliasStatement.getExpression() instanceof DotExpression) {
			IExpression de = aliasStatement.getExpression();
			lrl2 = lookup_dot_expression(aliasStatement.getContext(), (DotExpression) de);
			return lrl2.chooseBest(null);
		}
		lrl2 = aliasStatement.getContext().lookup(((IdentExpression) aliasStatement.getExpression()).getText());
		return lrl2.chooseBest(null);
	}

	private LookupResultList lookup_dot_expression(Context ctx, DotExpression de) {
		Stack<IExpression> s = dot_expression_to_stack(de);
		OS_Type t = null;
		IExpression ss = s.peek();
		while (!s.isEmpty()) {
			ss = s.peek();
			if (t != null && (t.getType() == OS_Type.Type.USER_CLASS || t.getType() == OS_Type.Type.FUNCTION))
				ctx = t.getClassOf().getContext();
			t = deduceExpression(ss, ctx);
			ss.setType(t);  // TODO should this be here?
			s.pop();
		}
		if (t == null) {
			NotImplementedException.raise();
			return new LookupResultList();
		} else
			return t.getElement().getParent().getContext().lookup(((IdentExpression)ss).getText());
	}

	@NotNull
	private Stack<IExpression> dot_expression_to_stack(DotExpression de) {
		Stack<IExpression> s = new Stack<IExpression>();
		IExpression e = de;
		IExpression left = null;
		s.push(de.getRight());
		while (true) {
			left = e.getLeft();
			s.push(left);
			if (!(left instanceof DotExpression)) break;
		}
		return s;
	}

	public OS_Type deduceExpression(@NotNull IExpression n, Context context) {
		if (n.getKind() == ExpressionKind.IDENT) {
			return deduceIdentExpression((IdentExpression)n, context);
		} else if (n.getKind() == ExpressionKind.NUMERIC) {
			return new OS_Type(BuiltInTypes.SystemInteger);
		} else if (n.getKind() == ExpressionKind.DOT_EXP) {
			DotExpression de = (DotExpression) n;
			LookupResultList lrl = lookup_dot_expression(context, de);
			OS_Type left_type = deduceExpression(de.getLeft(), context);
			OS_Type right_type = deduceExpression(de.getRight(), left_type.getClassOf().getContext());
			NotImplementedException.raise();
		} else if (n.getKind() == ExpressionKind.PROCEDURE_CALL) {
			deduceProcedureCall((ProcedureCallExpression) n, context);
			return n.getType();
		} else if (n.getKind() == ExpressionKind.QIDENT) {
			final IExpression expression = Helpers.qualidentToDotExpression2(((Qualident) n));
			return deduceExpression(expression, context);
		}

		return null;
	}

	private void deduceProcedureCall(ProcedureCallExpression pce, Context ctx) {
		throw new NotImplementedException();
	}

	private OS_Type deduceIdentExpression(IdentExpression ident, Context ctx) {
		throw new NotImplementedException();
	}

}

//
//
//
