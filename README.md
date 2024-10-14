# Ecliptic Seasons

![](https://media.forgecdn.net/avatars/thumbnails/1095/529/64/64/638640712387664433.png)

“节气”是Minecraft的一个中国风模组。

Ecliptic Seasons is a Chinese mod about seasons and weather.

### LICENSE 
*   For code: BSD-3
*   For resources: CC BY-NC-SA 4.0
*   Please do not port arbitrarily, communication can make the community better, and please respect our work.

### Wiki
Read the javaDocs in `EclipticSeasonsApi.java`.

### TODO
枯萎的花

isRainingAt 要区分下雨和下雪

月相

动态雨量

更高级的作物生长控制系统（https://zhuanlan.zhihu.com/p/685648430）

未来设计控制降雨和下雨的道具

注意如果tick rate 过快的话，会卡住，导致gettime % 这个写法失效
以及注意每个节气时长及下雨的关系

测试了Opt的兼容性，也许需要mixin或者mixin其

也许我们需要一个生云法杖

### New

新增了API构建并重构了目录。

新增了群系缓存查询

新增了冬季白色地图颜色匹配

新增了缓冲查询，避免河流沙滩气候与周围群系不同。

新增了永久改变覆雪状态的法杖。

新增了生存短期除雪方式，如打破方块或者使用扫帚。

### Credit ###

感谢方块小镇作者Coco几何和Xiao2对于蝴蝶模型的特别授权使用，此外蝴蝶在1.21后更改了实现方式，纹理和代码为本模组创作内容。

感谢雪真实的魔法作者雪尼Snowee对本模组的参考同意，此外本模组实际实现路径有所区别。

感谢Blur作者tterrag对模糊的实现，此外本模组在1.21后更改了实现。

感谢Illuminations作者RAT对萤火虫的实现，此外本模组参考了其部分设计思路并获得了作者同意。

春天的森林 许可:CC-BY 作者:梦醒时光 来源:耳聆网 https://www.ear0.com/sound/20322
公园风声 许可:CC-BY 作者:goodnames 来源:耳聆网 https://www.ear0.com/sound/40449
夜晚小河 许可:CC-BY 作者:goodnames 来源:耳聆网 https://www.ear0.com/sound/40451
风吹树叶 许可:CC0 作者:Nausicaa 来源:耳聆网 https://www.ear0.com/sound/17371
冬天的森林氛围 许可:CC0 作者:Caoilfhionn 来源:耳聆网 https://www.ear0.com/sound/14951
寒冷冬天的实地录音 许可:CC0 作者:Robert 来源:耳聆网 https://www.ear0.com/sound/12239

