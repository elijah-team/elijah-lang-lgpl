/*
 * Elijjah compiler, copyright Tripleo <oluoluolu+elijah@gmail.com>
 *
 * The contents of this library are released under the LGPL licence v3,
 * the GNU Lesser General Public License text was downloaded from
 * http://www.gnu.org/licenses/lgpl.html from `Version 3, 29 June 2007'
 *
 */
package tripleo.elijah.lang;

import org.junit.Assert;
import org.junit.Test;
import tripleo.elijah.util.Helpers;

import static org.easymock.EasyMock.*;

public class TypeOfTypeNameTest {

	@Test
	public void typeOfSimpleQualident() {
		//
		// CREATE MOCK
		//
		Context ctx = mock(Context.class);

		//
		// CREATE VARIABLES
		//
		String typeNameString = "AbstractFactory";

		VariableStatement var_x = new VariableStatement(null);
		var_x.setName(Helpers.string_to_ident("x")); // not necessary
		RegularTypeName rtn = new RegularTypeName(ctx);
		rtn.setName(string_to_qualident(typeNameString));
		var_x.setTypeName(rtn);

		LookupResultList lrl = new LookupResultList();
		lrl.add("x", 1, var_x, ctx);

		//
		// CREATE VARIABLE UNDER TEST
		//
		TypeOfTypeName t = new TypeOfTypeName(ctx);
		t.typeOf(string_to_qualident(var_x.getName()));

		//
		// SET UP EXPECTATIONS
		//
		expect(ctx.lookup(var_x.getName())).andReturn(lrl);
		replay(ctx);

		//
		// VERIFY EXPECTATIONS
		//
		TypeName tn = t.resolve(ctx);
//		System.out.println(tn);
		verify(ctx);
		Assert.assertEquals(typeNameString, tn.toString());
	}

	@Test
	public void typeOfComplexQualident() {
		//
		// CREATE MOCK
		//
		Context ctx = mock(Context.class);

		//
		// CREATE VARIABLES
		//
		String typeNameString = "package.AbstractFactory";

		VariableStatement var_x = new VariableStatement(null);
		var_x.setName(Helpers.string_to_ident("x")); // not necessary
		RegularTypeName rtn = new RegularTypeName(ctx);
		rtn.setName(string_to_qualident(typeNameString));
		var_x.setTypeName(rtn);

		LookupResultList lrl = new LookupResultList();
		lrl.add("x", 1, var_x, ctx);

		//
		// CREATE VARIABLE UNDER TEST
		//
		TypeOfTypeName t = new TypeOfTypeName(ctx);
		t.typeOf(string_to_qualident("x"));

		//
		// SET UP EXPECTATIONS
		//
		expect(ctx.lookup("x")).andReturn(lrl);
		replay(ctx);

		//
		// VERIFY EXPECTATIONS
		//
		TypeName tn = t.resolve(ctx);
//		System.out.println(tn);
		verify(ctx);
		Assert.assertEquals(typeNameString, tn.toString());
	}

	static private Qualident string_to_qualident(String x) {
		Qualident q = new Qualident();
		for (String xx : x.split("\\.")) {
			q.append(Helpers.makeToken(xx));
		}
		return q;
	}
}

//
//
//
