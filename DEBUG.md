# 伤害来源调试

在 `config/rendi-common.toml` 中开启：

```toml
[debug]
damageSourcesToChat = true
```

服务器每次收到有效的 `LivingIncomingDamageEvent`，会在所有在线玩家的聊天栏显示
`[ReNDI] DamageSource.msgId = …`。输出发生在排除规则判断之前，因此也能查看白名单中的伤害来源。

该值来自 `DamageSource.getMsgId()`，可直接用于 `exclusions.damageSrcWhitelist`。开关默认关闭，重新加载配置会立即更新；调试输出不会修改伤害、取消判定或无敌帧配置。
