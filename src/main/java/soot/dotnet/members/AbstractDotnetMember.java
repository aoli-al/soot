package soot.dotnet.members;

import soot.SootClass;
import soot.Value;
import soot.dotnet.types.DotnetBasicTypes;
import soot.jimple.NullConstant;
import soot.jimple.StringConstant;

/**
 * Represents a .NET Member of a .NET Type
 */
public abstract class AbstractDotnetMember implements DotnetTypeMember {

    /**
     * If we have specific return or assignment characteristics, rewrite it (mostly reftypes)
     * Due to the different eco system of .NET and unsafe methods
     * @param declaringClass
     * @param fieldMethodName
     * @return
     */
    public static Value checkRewriteCilSpecificMember(SootClass declaringClass, String fieldMethodName) {
        /*
          Normally System.String.Empty == Reftype(System.String), because is string, lead to errors in validation
          With this fix: System.String.Empty == StringConstant
         */
        if (declaringClass.getName().equals(DotnetBasicTypes.SYSTEM_STRING) && fieldMethodName.equals("Empty"))
            return StringConstant.v("");
        /*
            If System.Array.Empty, normal RefType(System.Array)
            Problem with System.Type[] = System.Array.Empty
            With this fix null constant
         */
        if (declaringClass.getName().equals(DotnetBasicTypes.SYSTEM_ARRAY) && fieldMethodName.equals("Empty")) {
            // return Jimple.v().newNewExpr(RefType.v(DotnetBasicTypes.SYSTEM_ARRAY));
            return NullConstant.v();
        }

        return null;
    }
}
