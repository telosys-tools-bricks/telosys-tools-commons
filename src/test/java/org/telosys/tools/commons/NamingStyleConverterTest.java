package org.telosys.tools.commons;

import static org.junit.Assert.*;

import org.junit.Test;

public class NamingStyleConverterTest {

	@Test
	public void toSnakeCaseTest() {
		
		NamingStyleConverter c = new NamingStyleConverter();
		assertEquals("", c.toSnakeCase(null));
		assertEquals("", c.toSnakeCase(""));
		
		// Expected use cases : from snake_case 
		assertEquals("my_var_name", c.toSnakeCase("my_var_name"));
		assertEquals("my_var_name", c.toSnakeCase("my_Var_Name"));

		// Expected use cases : from ANACONDA_CASE 
		assertEquals("my_var_name", c.toSnakeCase("MY_VAR_NAME"));
		
		// Expected use cases : from Cobra_Case 
		assertEquals("my_var_name", c.toSnakeCase("My_Var_Name"));
		
		// Expected use cases : from camelCase 
		assertEquals("my_var_name", c.toSnakeCase("myVarName"));
		assertEquals("my_x_y_position", c.toSnakeCase("myXYPosition"));
		assertEquals("my_x1_y2_position", c.toSnakeCase("myX1Y2Position"));

		// Expected use cases : from PascalCase 
		assertEquals("my_var_name", c.toSnakeCase("MyVarName"));
		
		assertEquals("abc", c.toSnakeCase("abc"));
		assertEquals("abc", c.toSnakeCase(" abc  "));
		assertEquals("abc", c.toSnakeCase("Abc"));
		assertEquals("a_b_c", c.toSnakeCase("ABC"));

		// Unexpected use cases 
		assertEquals("my_var_name", c.toSnakeCase("my Var Name"));
		assertEquals("my_var_name", c.toSnakeCase("My Var Name"));
		assertEquals("my_var_name", c.toSnakeCase(" My Var   Name  "));
		assertEquals("my_varname", c.toSnakeCase(" My Var   name  "));
		
		assertEquals("_my_var_name", c.toSnakeCase("_my_Var_Name"));
		assertEquals("_my_var_name_", c.toSnakeCase("_my_Var_Name_"));
		assertEquals("my__var___name", c.toSnakeCase("my__Var___Name"));

	}
	
	@Test
	public void toAnacondaCaseTest() {
		
		NamingStyleConverter c = new NamingStyleConverter();
		assertEquals("", c.toAnacondaCase(null));
		assertEquals("", c.toAnacondaCase(""));
		
		// Expected use cases : from snake_case 
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("my_var_name"));
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("my_Var_Name"));

		// Expected use cases : from ANACONDA_CASE 
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("MY_VAR_NAME"));
		
		// Expected use cases : from Cobra_Case 
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("My_Var_Name"));
		
		// Expected use cases : from camelCase 
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("myVarName"));
		assertEquals("MY_X_Y_POSITION", c.toAnacondaCase("myXYPosition"));
		assertEquals("MY_X1_Y2_POSITION", c.toAnacondaCase("myX1Y2Position"));

		// Expected use cases : from PascalCase 
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("MyVarName"));
		
		assertEquals("ABC", c.toAnacondaCase("abc"));
		assertEquals("ABC", c.toAnacondaCase(" abc  "));
		assertEquals("ABC", c.toAnacondaCase("Abc"));
		assertEquals("A_B_C", c.toAnacondaCase("ABC"));

		// Unexpected use cases 
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("my Var Name"));
		assertEquals("MY_VAR_NAME", c.toAnacondaCase("My Var Name"));
		assertEquals("MY_VAR_NAME", c.toAnacondaCase(" My Var   Name  "));
		assertEquals("MY_VARNAME", c.toAnacondaCase(" My Var   name  "));
		
		assertEquals("_MY_VAR_NAME", c.toAnacondaCase("_my_Var_Name"));
		assertEquals("_MY_VAR_NAME_", c.toAnacondaCase("_my_Var_Name_"));
		assertEquals("MY__VAR___NAME", c.toAnacondaCase("my__Var___Name"));
	}

	@Test
	public void toPascalCaseTest() {
		
		NamingStyleConverter c = new NamingStyleConverter();
		assertEquals("", c.toPascalCase(null));
		assertEquals("", c.toPascalCase(""));
		
		assertEquals("Abc", c.toPascalCase("abc"));
		assertEquals("Abc", c.toPascalCase("Abc"));
		assertEquals("AbcDefGhi", c.toPascalCase(" abcDefGhi  "));

		// Expected use cases : from snake_case 
		assertEquals("ABC", c.toPascalCase("a_b_c"));
		assertEquals("MyVarName", c.toPascalCase("my_var_name"));
		
		// Expected use cases : from Cobra_Case 
		assertEquals("MyVarName", c.toPascalCase("My_Var_Name"));

		// Expected use cases : from ANACONDA_CASE 
		assertEquals("ABC", c.toPascalCase("A_B_C"));
		assertEquals("MyVarName", c.toPascalCase("MY_VAR_NAME"));
		
		// Expected use cases : from camelCase 
		assertEquals("ABC", c.toPascalCase("aBC"));
		assertEquals("MyVarName", c.toPascalCase("myVarName"));

		// Expected use cases : from PascalCase 
		assertEquals("ABC", c.toPascalCase("ABC"));
		assertEquals("MyVarName", c.toPascalCase("MyVarName"));

		
		// Multiple separators
		assertEquals("MyVarName", c.toPascalCase("my__var___name"));
		assertEquals("MyVarName", c.toPascalCase("_my__var___name_"));
		assertEquals("MyVarName", c.toPascalCase("__my_var__name___"));


		// Spaces management 
		assertEquals("Abc", c.toPascalCase(" abc  "));
		assertEquals("MyVarName", c.toPascalCase("my_ var _name"));
		assertEquals("MyVarname", c.toPascalCase(" my _ var name "));
	}
	
	@Test
	public void toPascalCaseWithSeparatorTest() {
		
		NamingStyleConverter c = new NamingStyleConverter();
		assertEquals("", c.toPascalCase(null, "/"));
		assertEquals("", c.toPascalCase("", "/"));
		
		assertEquals("Abc", c.toPascalCase("abc", "/"));
		assertEquals("Abc", c.toPascalCase("Abc", "/"));
		assertEquals("AbcDefGhi", c.toPascalCase(" abcDefGhi  ", "/"));

		// Expected use cases 
		assertEquals("ABC", c.toPascalCase("a/b/c", "/") );
		assertEquals("MyVarName", c.toPascalCase("my/var/name", "/"));
		assertEquals("MyVarName", c.toPascalCase("my.var.name", "."));

		// already in expected case
		assertEquals("MyVarName", c.toPascalCase("MyVarName", "."));

		// Unexpected use cases 
		assertEquals("A_b_c", c.toPascalCase("a_b_c", "/") );
		assertEquals("My_var_name", c.toPascalCase("my_var_name", "/"));
		assertEquals("A/b/c", c.toPascalCase("a/b/c", null) );
		assertEquals("A/b/c", c.toPascalCase("a/b/c", "") );
	}
	
	@Test
	public void toCamelCaseTest() {
		NamingStyleConverter c = new NamingStyleConverter();
		assertEquals("", c.toCamelCase(null));
		assertEquals("", c.toCamelCase(""));
		
		assertEquals("abc", c.toCamelCase("abc"));
		assertEquals("abc", c.toCamelCase("Abc"));
		assertEquals("abcDefGhi", c.toCamelCase(" abcDefGhi  "));

		// Expected use cases : from snake_case 
		assertEquals("aBC", c.toCamelCase("a_b_c"));
		assertEquals("myVarName", c.toCamelCase("my_var_name"));
		
		// Expected use cases : from Cobra_Case 
		assertEquals("myVarName", c.toCamelCase("My_Var_Name"));

		// Expected use cases : from ANACONDA_CASE 
		assertEquals("aBC", c.toCamelCase("A_B_C"));
		assertEquals("myVarName", c.toCamelCase("MY_VAR_NAME"));
		
		// Expected use cases : from camelCase 
		assertEquals("aBC", c.toCamelCase("aBC"));
		assertEquals("myVarName", c.toCamelCase("myVarName"));

		// Expected use cases : from PascalCase 
		assertEquals("aBC", c.toCamelCase("ABC"));
		assertEquals("myVarName", c.toCamelCase("MyVarName"));

		// Multiple separators
		assertEquals("myVarName", c.toCamelCase("my__var___name"));
		assertEquals("myVarName", c.toCamelCase("_my__var___name_"));
		assertEquals("myVarName", c.toCamelCase("__my_var__name___"));

		// Spaces management 
		assertEquals("abc", c.toCamelCase(" abc  "));
		assertEquals("myVarName", c.toCamelCase("my_ var _name"));
		assertEquals("myVarname", c.toCamelCase(" my _ var name "));
	}

	@Test
	public void toCamelCaseWithSeparatorTest() {
		
		NamingStyleConverter c = new NamingStyleConverter();
		assertEquals("", c.toCamelCase(null, "/"));
		assertEquals("", c.toCamelCase("", "/"));
		
		assertEquals("abc", c.toCamelCase("abc", "/"));
		assertEquals("abc", c.toCamelCase("Abc", "/"));
		assertEquals("abcDefGhi", c.toCamelCase(" abcDefGhi  ", "/"));

		// Expected use cases 
		assertEquals("aBC", c.toCamelCase("a/b/c", "/") );
		assertEquals("myVarName", c.toCamelCase("my/var/name", "/"));
		assertEquals("myVarName", c.toCamelCase("my.var.name", "."));

		// Unexpected use cases 
		assertEquals("a_b_c", c.toCamelCase("a_b_c", "/") );
		assertEquals("my_var_name", c.toCamelCase("my_var_name", "/"));
		assertEquals("a/b/c", c.toCamelCase("a/b/c", null) );
		assertEquals("a/b/c", c.toCamelCase("a/b/c", "") );
	}
}

