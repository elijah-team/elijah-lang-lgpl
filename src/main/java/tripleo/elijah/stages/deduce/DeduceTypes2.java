/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.stages.deduce;

import tripleo.elijah.lang.*;
import tripleo.elijah.lang2.BuiltInTypes;
import tripleo.elijah.stages.gen_fn.*;
import tripleo.elijah.stages.instructions.*;
import tripleo.elijah.util.NotImplementedException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

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
							cte.getTypeTableEntry().attached = new OS_Type(BuiltInTypes.SystemInteger).resolve(context);
						}
						break;
					default:
						throw new NotImplementedException();
					}
				}
			}
				break;
			case X:
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
		}
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

	private void implement_calls(GeneratedFunction gf, Context ctx, InstructionArgument i2, ProcTableEntry fn1, int pc) {
		IExpression pn1 = fn1.expression;
		if (pn1 instanceof IdentExpression) {
			String pn = ((IdentExpression) pn1).getText();
			boolean found = lookup_name_calls(ctx, pn, fn1);
			LookupResultList lrl;
			OS_Element best;
			if (found) return;

			String pn2 = reverse_name(pn);
//			System.out.println("7002 "+pn2);
			found = lookup_name_calls(ctx, pn2, fn1);
			if (found) return;

			final VariableTableEntry vte = gf.getVarTableEntry(to_int(i2));
			LookupResultList lrl2 = gf.getContextFromPC(pc).lookup(vte.getName());
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
}

//
//
//
