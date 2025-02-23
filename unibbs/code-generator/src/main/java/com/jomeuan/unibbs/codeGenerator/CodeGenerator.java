package com.jomeuan.unibbs.codeGenerator;

import java.nio.file.Paths;

import org.apache.ibatis.type.JdbcType;

import com.baomidou.mybatisplus.generator.FastAutoGenerator;
import com.baomidou.mybatisplus.generator.config.rules.DbColumnType;

public class CodeGenerator {
        public static void main(String[] args) {
                FastAutoGenerator.create(
                                "jdbc:mysql://localhost:3306/uni_bbs?serverTimezone=Asia/Shanghai&useUnicode=true&characterEncoding=utf-8&zeroDateTimeBehavior=convertToNull&useSSL=false&allowPublicKeyRetrieval=true",
                                "root", "123456")
                                .globalConfig(builder -> builder
                                                .author("jomeuan")
                                                .outputDir(Paths.get(System.getProperty("user.dir"))
                                                                + "/unibbs")
                                                .commentDate("yyyy-MM-dd"))
                                .dataSourceConfig(builder -> builder
                                                .typeConvertHandler((globalConfig, typeRegistry, metaInfo) -> {
                                                        // 兼容旧版本转换成Integer
                                                        if (JdbcType.TINYINT == metaInfo.getJdbcType()) {
                                                                return DbColumnType.INTEGER;
                                                        }
                                                        return typeRegistry.getColumnType(metaInfo);
                                                }))
                                .packageConfig(builder -> builder
                                                .parent("com.jomeuan.unibbs")
                                                .entity("entity")
                                                .mapper("mapper")
                                                .service("service")
                                                .serviceImpl("service.impl")
                                                .xml("mapper.xml"))
                                .strategyConfig(builder -> builder
                                                .entityBuilder()
                                                .enableLombok())
                                .execute();
        }
}
