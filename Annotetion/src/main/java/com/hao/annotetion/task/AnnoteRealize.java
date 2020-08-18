package com.hao.annotetion.task;

import com.hao.annotetion.annotation.Bind;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.ParameterSpec;
import com.squareup.javapoet.ParameterizedTypeName;
import com.squareup.javapoet.TypeSpec;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedOptions;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

import static com.hao.annotetion.task.Config.routeClassPath;


@SupportedOptions("annote")
@SupportedAnnotationTypes({"com.hao.annotetion.annotation.Bind"})
@SupportedSourceVersion(SourceVersion.RELEASE_7)
public class AnnoteRealize extends AbstractProcessor {

    /**
     * 文件生成器 类/资源
     */
    private Filer filerUtils;

    /**
     * 节点工具类 (类、函数、属性都是节点)
     */
    private Elements elementUtils;

    /**
     * type(类信息)工具类
     */
    private Types typeUtils;


    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        filerUtils = processingEnvironment.getFiler();
        elementUtils = processingEnvironment.getElementUtils();
        typeUtils = processingEnvironment.getTypeUtils();

        Map<String, String> options = processingEnvironment.getOptions();
        if (options != null && !options.isEmpty()) {
            String moduleName = options.get("moduleName");
            System.out.println("注解绑定 初始化 " + moduleName);
        }
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        if (set != null && !set.isEmpty()) {
            //被Route注解的节点集合
            Set<? extends Element> rootElements = roundEnvironment.getElementsAnnotatedWith(Bind.class);
            //获得Activity这个类的节点信息
            TypeElement activity = elementUtils.getTypeElement("android.app.Activity");
            List<BindInfo> bindInfos = new ArrayList<>();
            for (Element element : rootElements) {
                //类信息
                TypeMirror typeMirror = element.asType();
                if (typeUtils.isSubtype(typeMirror, activity.asType())) {
                    BindInfo bindInfo = BindInfo.build(element.getClass(), element.getClass().toString().replace(".class", "").replace(".", "").toUpperCase(), Type.ACTIVITY);
                    bindInfo.setElement(element);
                    bindInfos.add(bindInfo);
                }
                System.out.println("注解绑定的东西：" + element.getSimpleName() + "     " + element.asType());
            }
            createFileSaveBind(bindInfos);
        }
        return true;
    }


    public void createFileSaveBind(List<BindInfo> bindInfos) {

        ParameterSpec altas = ParameterSpec.builder(new HashMap<String, BindInfo>().getClass(), "bindInfos").build();

        MethodSpec.Builder methodBuilder = MethodSpec.methodBuilder("loadInfo")
                .addModifiers(Modifier.PUBLIC)
                .addAnnotation(Override.class)
                .addParameter(altas);


        TypeSpec.Builder builder = TypeSpec.classBuilder("RouterContent")
                .addSuperinterface(BaseSaveInfo.class)
                .addModifiers(Modifier.PUBLIC);
        for (BindInfo bindInfo : bindInfos) {
            String classPath = ClassName.get((TypeElement) (bindInfo.getElement())).toString().replace(".class", "");
            System.out.println(classPath);
            String[] strings = classPath.split("\\.");
            String tag = strings[strings.length - 1].toUpperCase();
            FieldSpec newFiled = FieldSpec.builder(String.class, tag)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL, Modifier.STATIC)
                    .initializer("$S ", ClassName.get((TypeElement) (bindInfo.getElement())).toString().replace(".class", ""))
                    .build();
            builder.addField(newFiled);

            //函数体的添加
            methodBuilder.addStatement("bindInfos.put(" + tag + ",$T.build($T.class," + tag + ",$T.$L))",
                    ClassName.get(BindInfo.class),
                    ClassName.get(((TypeElement) bindInfo.getElement())),
                    ClassName.get(Type.class),
                    bindInfo.getType());
        }
        TypeSpec typeSpec = builder
                .addMethod(methodBuilder.build())
                .build();
        JavaFile javaFile = JavaFile.builder(routeClassPath, typeSpec).build();
        try {
            javaFile.writeTo(filerUtils);
        } catch (IOException e) {
            e.printStackTrace();
        }
//
    }

}
