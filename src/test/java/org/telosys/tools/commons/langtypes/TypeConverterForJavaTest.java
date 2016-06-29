package org.telosys.tools.commons.langtypes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.telosys.tools.commons.langtypes.AttributeTypeInfo.NONE;
import static org.telosys.tools.commons.langtypes.AttributeTypeInfo.NOT_NULL;
import static org.telosys.tools.commons.langtypes.AttributeTypeInfo.OBJECT_TYPE;
import static org.telosys.tools.commons.langtypes.AttributeTypeInfo.PRIMITIVE_TYPE;
import static org.telosys.tools.commons.langtypes.AttributeTypeInfo.SQL_TYPE;
import static org.telosys.tools.commons.langtypes.AttributeTypeInfo.UNSIGNED_TYPE;

import java.math.BigDecimal;

import org.junit.Test;

public class TypeConverterForJavaTest  {

	private LanguageType getType(TypeConverter tc, AttributeTypeInfo typeInfo ) {
		LanguageType lt = tc.getType(typeInfo);
		System.out.println( typeInfo + " --> " + lt );
		return lt ;
	}
	
//	private LanguageType getType(TypeConverter tc, String neutralType ) {
//		AttributeTypeInfo typeInfo = new AttributeTypeInfo(neutralType, false, false, false, false, false);
//		return getType(tc, typeInfo);
//	}
//	
//	private LanguageType getTypeNotNull(TypeConverter tc, String neutralType ) {
//		AttributeTypeInfo typeInfo = new AttributeTypeInfo(neutralType, true, false, false, false, false);
//		return getType(tc, typeInfo);
//	}
//
//	private LanguageType getPrimitiveType(TypeConverter tc, String neutralType ) {
//		AttributeTypeInfo typeInfo = new AttributeTypeInfo(neutralType, false, true, false, false, false);
//		return getType(tc, typeInfo);
//	}
//
//	private LanguageType getUnsignedType(TypeConverter tc, String neutralType ) {
//		AttributeTypeInfo typeInfo = new AttributeTypeInfo(neutralType, false, false, false, true, false);
//		return getType(tc, typeInfo);
//	}
//
//	private LanguageType getObjectType(TypeConverter tc, String neutralType ) {
//		AttributeTypeInfo typeInfo = new AttributeTypeInfo(neutralType, false, false, true, false, false);
//		return getType(tc, typeInfo);
//	}
//
//	private LanguageType getNotNullObjectType(TypeConverter tc, String neutralType ) {
//		AttributeTypeInfo typeInfo = new AttributeTypeInfo(neutralType, true, false, true, false, false);
//		return getType(tc, typeInfo);
//	}

	private LanguageType getType(TypeConverter tc, String neutralType, int typeInfo ) {
		AttributeTypeInfo attributeTypeInfo = new AttributeTypeInfo(neutralType, typeInfo);
		return getType(tc, attributeTypeInfo);
	}
	
	private void check( LanguageType lt, Class<?> clazz ) {
		assertNotNull(lt);
		assertEquals(clazz.getSimpleName(), lt.getSimpleType() );
		assertEquals(clazz.getCanonicalName(), lt.getFullType() );
	}
	
	@Test
	public void testString() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		check( getType(tc, NeutralType.STRING, NONE ), String.class);
		check( getType(tc, NeutralType.STRING, NOT_NULL ), String.class);
		
		check( getType(tc, NeutralType.STRING, PRIMITIVE_TYPE ), String.class);
		check( getType(tc, NeutralType.STRING, UNSIGNED_TYPE ), String.class);
		check( getType(tc, NeutralType.STRING, PRIMITIVE_TYPE + UNSIGNED_TYPE ), String.class);
		
		check( getType(tc, NeutralType.STRING, OBJECT_TYPE), String.class);
		check( getType(tc, NeutralType.STRING, SQL_TYPE ), String.class);
		check( getType(tc, NeutralType.STRING, OBJECT_TYPE + SQL_TYPE), String.class);
	}

	@Test
	public void testBoolean() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		check( getType(tc, NeutralType.BOOLEAN, NONE ), Boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, NOT_NULL ), boolean.class);
		
		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE ), boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, UNSIGNED_TYPE ), boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, PRIMITIVE_TYPE + UNSIGNED_TYPE ), boolean.class);
		
		check( getType(tc, NeutralType.BOOLEAN, OBJECT_TYPE ), Boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, SQL_TYPE ), Boolean.class);		
		check( getType(tc, NeutralType.BOOLEAN, NOT_NULL + OBJECT_TYPE ), Boolean.class);
		check( getType(tc, NeutralType.BOOLEAN, NOT_NULL + SQL_TYPE ), Boolean.class);
	}

	@Test
	public void testShort() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		LanguageType lt ; 
		
		//-----------------------------------
		lt = getType(tc, NeutralType.SHORT, NONE );
		check(lt, Short.class);		
		lt = getType(tc, NeutralType.SHORT, NOT_NULL );
		check(lt, short.class);		
		
		lt = getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE );
		check(lt, short.class);		
		lt = getType(tc, NeutralType.SHORT, NOT_NULL + PRIMITIVE_TYPE );
		check(lt, short.class);		
		lt = getType(tc, NeutralType.SHORT, UNSIGNED_TYPE );
		
		check(lt, short.class);		
		lt = getType(tc, NeutralType.SHORT, OBJECT_TYPE );
		check(lt, Short.class);		
		lt = getType(tc, NeutralType.SHORT, SQL_TYPE );
		check(lt, Short.class);		
		lt = getType(tc, NeutralType.SHORT, OBJECT_TYPE + SQL_TYPE);
		check(lt, Short.class);		
		
		lt = getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE + OBJECT_TYPE); // not compatible (primitive type has priority)
		check(lt, short.class);		
		lt = getType(tc, NeutralType.SHORT, PRIMITIVE_TYPE + SQL_TYPE); // not compatible (primitive type has priority)
		check(lt, short.class);		
		lt = getType(tc, NeutralType.SHORT, UNSIGNED_TYPE + OBJECT_TYPE ); // not compatible (primitive type has priority)
		check(lt, short.class);		
		lt = getType(tc, NeutralType.SHORT, UNSIGNED_TYPE + SQL_TYPE ); // not compatible (primitive type has priority)
		check(lt, short.class);		
	}

	@Test
	public void testDecimal() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Supposed to always return BigDecimal (in any cases) 
		check( getType(tc, NeutralType.DECIMAL, NONE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, NOT_NULL ), BigDecimal.class);
		
		check( getType(tc, NeutralType.DECIMAL, PRIMITIVE_TYPE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, UNSIGNED_TYPE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, PRIMITIVE_TYPE + UNSIGNED_TYPE ), BigDecimal.class);
		
		check( getType(tc, NeutralType.DECIMAL, OBJECT_TYPE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, SQL_TYPE ), BigDecimal.class);		
		check( getType(tc, NeutralType.DECIMAL, NOT_NULL + OBJECT_TYPE ), BigDecimal.class);
		check( getType(tc, NeutralType.DECIMAL, NOT_NULL + SQL_TYPE ), BigDecimal.class);
	}

	@Test
	public void testDate() {
		System.out.println("--- ");
		TypeConverter tc = new TypeConverterForJava() ;
		
		// Supposed to always return BigDecimal (in any cases) 
		check( getType(tc, NeutralType.DATE, NONE ), java.util.Date.class);
		check( getType(tc, NeutralType.DATE, NOT_NULL ), java.util.Date.class);
		
		check( getType(tc, NeutralType.DATE, PRIMITIVE_TYPE ), java.util.Date.class);
		check( getType(tc, NeutralType.DATE, UNSIGNED_TYPE ), java.util.Date.class);
		check( getType(tc, NeutralType.DATE, PRIMITIVE_TYPE + UNSIGNED_TYPE ), java.util.Date.class);
		
		check( getType(tc, NeutralType.DATE, OBJECT_TYPE ), java.util.Date.class);
		check( getType(tc, NeutralType.DATE, NOT_NULL + OBJECT_TYPE ), java.util.Date.class);

		check( getType(tc, NeutralType.DATE, SQL_TYPE ), java.sql.Date.class);	 // SQL Date	
		check( getType(tc, NeutralType.DATE, NOT_NULL + SQL_TYPE ), java.sql.Date.class); // SQL Date	
		check( getType(tc, NeutralType.DATE, OBJECT_TYPE + SQL_TYPE ), java.sql.Date.class); // SQL Date	
		check( getType(tc, NeutralType.DATE, PRIMITIVE_TYPE + SQL_TYPE ), java.sql.Date.class); // // not compatible (no Prim type => SQL Date)	
	}

}
