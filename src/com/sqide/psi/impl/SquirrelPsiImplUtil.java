package com.sqide.psi.impl;

import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.psi.*;
import com.intellij.psi.search.FileTypeIndex;
import com.intellij.psi.search.GlobalSearchScope;
import com.intellij.psi.util.PsiTreeUtil;
import com.intellij.util.indexing.FileBasedIndex;
import com.sqide.SquirrelFileType;
import com.sqide.psi.*;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class SquirrelPsiImplUtil {

    public static PsiElement setName(SquirrelFunctionDeclaration element, String newName) {
//        ASTNode keyNode = element.getNode().findChildByType(SquirrelTokenTypes.KEY);
//        if (keyNode != null) {
//            SquirrelFunctionName funcName = new SquirrelFunctionNameImpl(element.getNode()).;
//            funcName.setName(newName);
//            ASTNode newKeyNode = funcName.getFirstChild().getNode();
//            element.getNode().replaceChild(keyNode, newKeyNode);
//        }
        return element;
    }

    public static PsiElement getNameIdentifier(SquirrelFunctionDeclaration element) {
        return element.getFunctionName();
    }


    public static PsiReference getReference(SquirrelId element) {
        return new SquirrelFunctionDeclarationPsiReferenceBase(element);
    }

    public static class SquirrelFunctionDeclarationPsiReferenceBase extends PsiPolyVariantReferenceBase<PsiElement> {

        SquirrelFunctionDeclarationPsiReferenceBase(PsiElement element) {
            super(element);
        }

        @NotNull
        @Override
        public Object[] getVariants() {
            return new Object[0];
        }

        @NotNull
        @Override
        public ResolveResult[] multiResolve(boolean b) {
            List<ResolveResult> result = new ArrayList<>();
            String id = myElement.getText();

            Collection<VirtualFile> virtualFiles =
                    FileBasedIndex.getInstance().getContainingFiles(FileTypeIndex.NAME, SquirrelFileType.INSTANCE,
                            GlobalSearchScope.allScope(myElement.getProject()));

            for (VirtualFile vf : virtualFiles) {

                try {
                    SquirrelFile squirrelFile = (SquirrelFile) PsiManager.getInstance(myElement.getProject()).findFile(vf);
                    if (squirrelFile != null) {
                        try {
                            Collection<SquirrelFunctionDeclaration> functionDeclarations = PsiTreeUtil.findChildrenOfType(squirrelFile, SquirrelFunctionDeclaration.class);
                            for (SquirrelFunctionDeclaration functionDeclaration : functionDeclarations) {
                                SquirrelFunctionName functionName1 = functionDeclaration.getFunctionName();
                                if (functionName1 != null) {
                                    if (id.equals(functionName1.getText())) {
                                        result.add(new PsiElementResolveResult(functionName1));
                                    }
                                }
                            }

                            if (result.isEmpty()) {
                                Collection<SquirrelMethodDeclaration> methodDeclarations = PsiTreeUtil.findChildrenOfType(squirrelFile, SquirrelMethodDeclaration.class);
                                for (SquirrelMethodDeclaration methodDeclaration : methodDeclarations) {
                                    SquirrelFunctionName methodName1 = methodDeclaration.getFunctionName();
                                    if (id.equals(methodName1.getText())) {
                                        result.add(new PsiElementResolveResult(methodName1));
                                    }
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    throw e;
                }
            }
            return result.toArray(new ResolveResult[0]);
        }
    }
}