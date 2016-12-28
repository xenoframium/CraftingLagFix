package xenoframium.craftinglagfix.asm;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ConstructorMethodVisitor extends MethodVisitor{

	boolean hasInvokedSuperConstructor = false;
	boolean hasFinishedInit = false;
	
	public ConstructorMethodVisitor(int arg0) {
		super(arg0);
	}
	
	public ConstructorMethodVisitor(int arg0, MethodVisitor mv) {
		super(arg0, mv);
	}
	
	@Override
	public void visitLabel(Label label) {
		if (hasInvokedSuperConstructor && !hasFinishedInit) {
			return;
		}
		
		super.visitLabel(label);
	}
	
	@Override
	public void visitLineNumber(int line, Label start) {
		if (hasInvokedSuperConstructor && !hasFinishedInit) {
			return;
		}
		
		super.visitLineNumber(line, start);
	}

	@Override
	public void visitVarInsn(int opcode, int var) {
		if (hasInvokedSuperConstructor && !hasFinishedInit) {
			return;
		}
		
		super.visitVarInsn(opcode, var);
	}

	@Override
	public void visitTypeInsn(int opcode, String type) {
		if (hasInvokedSuperConstructor && !hasFinishedInit) {
			return;
		}
		
		super.visitTypeInsn(opcode, type);
	}

	public void visitInsn(int opcode) {
		if (hasInvokedSuperConstructor && !hasFinishedInit) {
			return;
		}
		
		super.visitInsn(opcode);
	}

	@Override
	public void visitFieldInsn(int opcode, String owner, String name, String desc) {
		if (opcode == Opcodes.PUTFIELD && owner.equals(ClassTransformer.craftingManagerClass.get()) && name.equals(ClassTransformer.recipesField.get()) && desc.equals("Ljava/util/List;")) {
			mv.visitVarInsn(Opcodes.ALOAD, 0);
			mv.visitFieldInsn(Opcodes.GETSTATIC, "xenoframium/craftinglagfix/CraftingRegistry", "recipes", "Ljava/util/List;");
			mv.visitFieldInsn(Opcodes.PUTFIELD, ClassTransformer.craftingManagerClass.get(), ClassTransformer.recipesField.get(), "Ljava/util/List;");
			hasFinishedInit = true;
			return;
		}
		
		super.visitFieldInsn(opcode, owner, name, desc);
	}
	
	@Override
	public void visitMethodInsn(int arg0, String arg1, String arg2, String arg3, boolean arg4) {
		if (hasInvokedSuperConstructor && !hasFinishedInit) {
			return;
		}
		
		if (arg0 == Opcodes.INVOKESPECIAL && arg1.equals("java/lang/Object") && arg2.equals("<init>") && arg3.equals("()V") && arg4 == false) {
			hasInvokedSuperConstructor = true;
		}
		
		super.visitMethodInsn(arg0, arg1, arg2, arg3, arg4);
	}

}
