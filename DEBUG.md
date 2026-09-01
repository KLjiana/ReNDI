# 伤害来源调试

在 `config/rendi-common.toml` 中开启：

```toml
[debug]
damageSourcesToChat = true
```

服务器每次收到有效的 `LivingHurtEvent`，会在在线玩家的聊天栏显示 `[ReNDI] DamageSource.msgId = …`。包含已被本模组排除的伤害，便于查询 `lava`、`onFire` 或其他模组提供的实际字符串，填写 `exclusions.damageSrcWhitelist`。

该值是 `DamageSource.getMsgId()`，不是伤害类型的注册资源 ID。开关默认关闭，运行中修改配置可重新加载；关闭后停止输出。调试输出不改变原有伤害、取消判定和无敌帧配置。
