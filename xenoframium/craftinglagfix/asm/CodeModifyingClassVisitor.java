package xenoframium.craftinglagfix.asm;

import static xenoframium.craftinglagfix.asm.ClassTransformer.*;
import static org.objectweb.asm.Opcodes.*;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;

import net.minecraft.item.crafting.ShapelessRecipes;
import net.minecraftforge.oredict.OreDictionary;

public class CodeModifyingClassVisitor extends ClassVisitor {

	public CodeModifyingClassVisitor(int api) {
		super(api);
	}

	public CodeModifyingClassVisitor(int api, ClassVisitor cv) {
		super(ASM4, cv);
	}

	@Override
	public MethodVisitor visitMethod(int access, String name, String desc, String signature, String[] exceptions) {
		
		if (name.equals(ClassTransformer.findMatchingRecipe.get()) && desc.equals(
				"(L" + inventoryCraftingClass.get() + ";L" + worldClass.get() + ";)L" + itemStackClass.get() + ";")) {
			return null;
		} else if (name.equals("<init>") && desc.equals("()V")) {
			MethodVisitor mv = super.visitMethod(access, name, desc, signature, exceptions);
			return new ConstructorMethodVisitor(ASM4, mv);
		}
		return super.visitMethod(access, name, desc, signature, exceptions);
	}

}
