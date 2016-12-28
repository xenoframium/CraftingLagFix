package xenoframium.craftinglagfix.asm;

import java.util.Iterator;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import static org.objectweb.asm.Opcodes.*;
import org.objectweb.asm.Type;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

import net.minecraft.launchwrapper.IClassTransformer;

public class ClassTransformer implements IClassTransformer{
	
	static class Name {
		static boolean obfuscated = false;
		
		String obf;
		String name;
		
		Name(String name, String obf) {
			this.obf = obf;
			this.name = name;
		}
		
		String get() {
			if (obfuscated) {
				return obf;
			} else {
				return name;
			}
		}
	}
	
	public static Name craftingManagerClass = new Name("net/minecraft/item/crafting/CraftingManager", "afe");
	public static Name itemStackClass = new Name("net/minecraft/item/ItemStack", "add");
	public static Name shapedRecipesClass = new Name("net/minecraft/item/crafting/ShapedRecipes", "afh");
	public static Name inventoryCraftingClass = new Name("net/minecraft/inventory/InventoryCrafting", "aae");
	public static Name worldClass = new Name("net/minecraft/world/World", "ahb");
	public static Name itemClass = new Name("net/minecraft/item/Item", "adb");
	public static Name blockClass = new Name("net/minecraft/block/Block", "aji");
	public static Name shapelessRecipesClass = new Name("net/minecraft/item/crafting/ShapelessRecipes", "afi");
	
	public static Name iRecipeInterface = new Name("net/minecraft/item/crafting/IRecipe", "afg");
	
	public static Name addRecipeMethod = new Name("addRecipe", "a");
	public static Name addShapelessRecipeMethod = new Name("addShapelessRecipe", "b");
	public static Name findMatchingRecipe = new Name("findMatchingRecipe", "a");
	public static Name getStackInSlotMethod = new Name("getStackInSlot", "a");
	public static Name getSizeInventoryMethod = new Name("getSizeInventory", "a");
	public static Name getItemMethod = new Name("getItem", "b");
	public static Name getMaxDamageMethod = new Name("getMaxDamage", "o");
	public static Name getItemDamageForDisplayMethod = new Name("getItemDamageForDisplay", "j");
	public static Name matchesMethod = new Name("matches", "a");
	public static Name getCraftingResultMethod = new Name("getCraftingResult", "a");
	public static Name getRecipeListMethod = new Name("getRecipeList", "b");
	
	public static Name recipesField = new Name("recipes", "b");
	public static Name stackSizeField = new Name("stackSize", "b");

	@Override
	public byte[] transform(String name, String transformedName, byte[] basicClass) {
		if (name.equals("net.minecraft.item.crafting.CraftingManager") || name.equals(craftingManagerClass.obf)) {
			if (name.equals(craftingManagerClass.obf)) {
				Name.obfuscated = true;
			}

			ClassReader classReader = new ClassReader(basicClass);
			ClassWriter classWriter = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
			CodeModifyingClassVisitor cv = new CodeModifyingClassVisitor(ASM4, classWriter);
			classReader.accept(cv, 0);
			
			MethodVisitor mv = classWriter.visitMethod(ACC_PUBLIC, findMatchingRecipe.get(), "(L"+inventoryCraftingClass.get()+";L"+worldClass.get()+";)L"+itemStackClass.get()+";", null, null);
			findMatchingRecipe(mv);
			
			return classWriter.toByteArray();
		}
		
		return basicClass;
	}
	
	private void findMatchingRecipe(MethodVisitor mv) {
		mv.visitCode();
		Label l0 = new Label();
		mv.visitLabel(l0);
		mv.visitLineNumber(288, l0);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 3);
		Label l1 = new Label();
		mv.visitLabel(l1);
		mv.visitLineNumber(289, l1);
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 4);
		Label l2 = new Label();
		mv.visitLabel(l2);
		mv.visitLineNumber(290, l2);
		mv.visitInsn(ACONST_NULL);
		mv.visitVarInsn(ASTORE, 5);
		Label l3 = new Label();
		mv.visitLabel(l3);
		mv.visitLineNumber(293, l3);
		mv.visitInsn(ICONST_0);
		mv.visitVarInsn(ISTORE, 6);
		Label l4 = new Label();
		mv.visitLabel(l4);
		Label l5 = new Label();
		mv.visitJumpInsn(GOTO, l5);
		Label l6 = new Label();
		mv.visitLabel(l6);
		mv.visitLineNumber(295, l6);
		mv.visitFrame(F_FULL, 7, new Object[] {craftingManagerClass.get(), inventoryCraftingClass.get(), worldClass.get(), INTEGER, itemStackClass.get(), itemStackClass.get(), INTEGER}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ILOAD, 6);
		mv.visitMethodInsn(INVOKEVIRTUAL, inventoryCraftingClass.get(), getStackInSlotMethod.get(), "(I)L"+itemStackClass.get()+";", false);
		mv.visitVarInsn(ASTORE, 7);
		Label l7 = new Label();
		mv.visitLabel(l7);
		mv.visitLineNumber(297, l7);
		mv.visitVarInsn(ALOAD, 7);
		Label l8 = new Label();
		mv.visitJumpInsn(IFNULL, l8);
		Label l9 = new Label();
		mv.visitLabel(l9);
		mv.visitLineNumber(299, l9);
		mv.visitVarInsn(ILOAD, 3);
		Label l10 = new Label();
		mv.visitJumpInsn(IFNE, l10);
		Label l11 = new Label();
		mv.visitLabel(l11);
		mv.visitLineNumber(301, l11);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitVarInsn(ASTORE, 4);
		mv.visitLabel(l10);
		mv.visitLineNumber(304, l10);
		mv.visitFrame(F_APPEND,1, new Object[] {itemStackClass.get()}, 0, null);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitInsn(ICONST_1);
		Label l12 = new Label();
		mv.visitJumpInsn(IF_ICMPNE, l12);
		Label l13 = new Label();
		mv.visitLabel(l13);
		mv.visitLineNumber(306, l13);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitVarInsn(ASTORE, 5);
		mv.visitLabel(l12);
		mv.visitLineNumber(309, l12);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitIincInsn(3, 1);
		mv.visitLabel(l8);
		mv.visitLineNumber(293, l8);
		mv.visitFrame(F_CHOP,1, null, 0, null);
		mv.visitIincInsn(6, 1);
		mv.visitLabel(l5);
		mv.visitFrame(F_SAME, 0, null, 0, null);
		mv.visitVarInsn(ILOAD, 6);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEVIRTUAL, inventoryCraftingClass.get(), getSizeInventoryMethod.get(), "()I", false);
		mv.visitJumpInsn(IF_ICMPLT, l6);
		Label l14 = new Label();
		mv.visitLabel(l14);
		mv.visitLineNumber(313, l14);
		mv.visitVarInsn(ILOAD, 3);
		mv.visitInsn(ICONST_2);
		Label l15 = new Label();
		mv.visitJumpInsn(IF_ICMPNE, l15);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemMethod.get(), "()L"+itemClass.get()+";", false);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemMethod.get(), "()L"+itemClass.get()+";", false);
		mv.visitJumpInsn(IF_ACMPNE, l15);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitFieldInsn(GETFIELD, itemStackClass.get(), stackSizeField.get(), "I");
		mv.visitInsn(ICONST_1);
		mv.visitJumpInsn(IF_ICMPNE, l15);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitFieldInsn(GETFIELD, itemStackClass.get(), stackSizeField.get(), "I");
		mv.visitInsn(ICONST_1);
		mv.visitJumpInsn(IF_ICMPNE, l15);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemMethod.get(), "()L"+itemClass.get()+";", false);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemClass.get(), "isRepairable", "()Z", false);
		mv.visitJumpInsn(IFEQ, l15);
		Label l16 = new Label();
		mv.visitLabel(l16);
		mv.visitLineNumber(315, l16);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemMethod.get(), "()L"+itemClass.get()+";", false);
		mv.visitVarInsn(ASTORE, 7);
		Label l17 = new Label();
		mv.visitLabel(l17);
		mv.visitLineNumber(316, l17);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemClass.get(), getMaxDamageMethod.get(), "()I", false);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemDamageForDisplayMethod.get(), "()I", false);
		mv.visitInsn(ISUB);
		mv.visitVarInsn(ISTORE, 8);
		Label l18 = new Label();
		mv.visitLabel(l18);
		mv.visitLineNumber(317, l18);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemClass.get(), getMaxDamageMethod.get(), "()I", false);
		mv.visitVarInsn(ALOAD, 5);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemDamageForDisplayMethod.get(), "()I", false);
		mv.visitInsn(ISUB);
		mv.visitVarInsn(ISTORE, 9);
		Label l19 = new Label();
		mv.visitLabel(l19);
		mv.visitLineNumber(318, l19);
		mv.visitVarInsn(ILOAD, 8);
		mv.visitVarInsn(ILOAD, 9);
		mv.visitInsn(IADD);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemClass.get(), getMaxDamageMethod.get(), "()I", false);
		mv.visitInsn(ICONST_5);
		mv.visitInsn(IMUL);
		mv.visitIntInsn(BIPUSH, 100);
		mv.visitInsn(IDIV);
		mv.visitInsn(IADD);
		mv.visitVarInsn(ISTORE, 10);
		Label l20 = new Label();
		mv.visitLabel(l20);
		mv.visitLineNumber(319, l20);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemClass.get(), getMaxDamageMethod.get(), "()I", false);
		mv.visitVarInsn(ILOAD, 10);
		mv.visitInsn(ISUB);
		mv.visitVarInsn(ISTORE, 11);
		Label l21 = new Label();
		mv.visitLabel(l21);
		mv.visitLineNumber(321, l21);
		mv.visitVarInsn(ILOAD, 11);
		Label l22 = new Label();
		mv.visitFrame(F_FULL, 12, new Object[] {craftingManagerClass.get(), ""+inventoryCraftingClass.get()+"", ""+worldClass.get()+"", INTEGER, itemStackClass.get(), itemStackClass.get(), INTEGER, ""+itemClass.get()+"", INTEGER, INTEGER, INTEGER, INTEGER}, 0, new Object[] {});
		mv.visitTypeInsn(NEW, itemStackClass.get());
		mv.visitInsn(DUP);
		mv.visitVarInsn(ALOAD, 4);
		mv.visitMethodInsn(INVOKEVIRTUAL, itemStackClass.get(), getItemMethod.get(), "()L"+itemClass.get()+";", false);
		mv.visitInsn(ICONST_1);
		mv.visitVarInsn(ILOAD, 11);
		mv.visitMethodInsn(INVOKESPECIAL, itemStackClass.get(), "<init>", "(L"+itemClass.get()+";II)V", false);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l15);
		mv.visitLineNumber(329, l15);
		mv.visitFrame(F_FULL, 7, new Object[] {craftingManagerClass.get(), ""+inventoryCraftingClass.get()+"", ""+worldClass.get()+"", INTEGER, itemStackClass.get(), itemStackClass.get(), INTEGER}, 0, new Object[] {});
		mv.visitVarInsn(ALOAD, 1);
		mv.visitVarInsn(ALOAD, 2);
		mv.visitMethodInsn(INVOKESTATIC, "xenoframium/craftinglagfix/CraftingRegistry", "getRecipe", "(L"+inventoryCraftingClass.get()+";L"+worldClass.get()+";)L"+iRecipeInterface.get()+";", false);
		mv.visitVarInsn(ASTORE, 7);
		Label l24 = new Label();
		mv.visitLabel(l24);
		mv.visitLineNumber(331, l24);
		mv.visitVarInsn(ALOAD, 7);
		Label l25 = new Label();
		mv.visitJumpInsn(IFNONNULL, l25);
		Label l26 = new Label();
		mv.visitLabel(l26);
		mv.visitLineNumber(332, l26);
		mv.visitInsn(ACONST_NULL);
		mv.visitInsn(ARETURN);
		mv.visitLabel(l25);
		mv.visitLineNumber(335, l25);
		mv.visitFrame(F_APPEND,1, new Object[] {""+iRecipeInterface.get()+""}, 0, null);
		mv.visitVarInsn(ALOAD, 7);
		mv.visitVarInsn(ALOAD, 1);
		mv.visitMethodInsn(INVOKEINTERFACE, ""+iRecipeInterface.get()+"", getCraftingResultMethod.get(), "(L"+inventoryCraftingClass.get()+";)L"+itemStackClass.get()+";", true);
		mv.visitInsn(ARETURN);
		Label l27 = new Label();
		mv.visitLabel(l27);
		mv.visitLocalVariable("this", "L"+craftingManagerClass.get()+";", null, l0, l27, 0);
		mv.visitLocalVariable("par1InventoryCrafting", "L"+inventoryCraftingClass.get()+";", null, l0, l27, 1);
		mv.visitLocalVariable("par2World", "L"+worldClass.get()+";", null, l0, l27, 2);
		mv.visitLocalVariable("i", "I", null, l1, l27, 3);
		mv.visitLocalVariable("itemstack", "L"+itemStackClass.get()+";", null, l2, l27, 4);
		mv.visitLocalVariable("itemstack1", "L"+itemStackClass.get()+";", null, l3, l27, 5);
		mv.visitLocalVariable("j", "I", null, l4, l27, 6);
		mv.visitLocalVariable("itemstack2", "L"+itemStackClass.get()+";", null, l7, l8, 7);
		mv.visitLocalVariable("item", "L"+itemClass.get()+";", null, l17, l15, 7);
		mv.visitLocalVariable("j1", "I", null, l18, l15, 8);
		mv.visitLocalVariable("k", "I", null, l19, l15, 9);
		mv.visitLocalVariable("l", "I", null, l20, l15, 10);
		mv.visitLocalVariable("i1", "I", null, l21, l15, 11);
		mv.visitLocalVariable("recipe", "L"+iRecipeInterface.get()+";", null, l24, l27, 7);
		mv.visitMaxs(5, 12);
		mv.visitEnd();
	}
}
