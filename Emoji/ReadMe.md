# H1 添加依赖

        <dependency>
            <groupId>com.vdurmont</groupId>
            <artifactId>emoji-java</artifactId>
            <version>4.0.0</version>
        </dependency>

# API 处理

对象	静态方法	描述

EmojiParser	parseToAliases(String input)	字符串input中所有emoji表情字符转化为别名


parseToUnicode(String input)	字符串input中所有emoji表情字符转化为统一码

parseToHtmlDecimal(String input)	字符串input中所有emoji表情字符转化为html数字编码，例如: 🐯



parseToHtmlHexadecimal(String input)	字符串input中所有emoji表情字符转化为htm十六进制编码，例如: 🐯

removeAllEmojis(String str)	移除字符串中所有 emoji表情字符

removeEmojis(String str,final Collection<Emoji> emojisToRemove)  移除字符串中多个 emoji表情字符，指定要移除的emoji表情字符的集合

removeAllEmojisExcept(String str, final Collection<Emoji> emojisToKeep)  移除字符串中多个 emoji表情字符，指定要保留的emoji表情字符的集合

parseFromUnicode(String input, EmojiParser.EmojiTransformer transformer)	自定义解析转换统一码 emoji表情字符

extractEmojis(String input)	提取字符串中的所有emoji表情字符

EmojiManager	getForTag(String tag)	通过标签获取 emoji表情对象

getForAlias(String alias)	通过别名获取 emoji表情对象

getByUnicode(String unicode)	通过统一码获取 emoji表情对象

getAll()	获取所有 emoji表情对象

isEmoji(String string)	判断是否为 一个emoji表情

isOnlyEmojis(String string)	判断字符串是否仅有emoji表情 

isEmoji(char[] sequence)	判断序列是否为一个emoji表情

getAllTags()	获取所有标签

EmojiLoader	loadEmojis(InputStream stream)	输入流加载emoji表情集合



Emoji Hutool 文档: https://apidoc.gitee.com/dromara/hutool/