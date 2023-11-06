-- Active: 1699255920118@@127.0.0.1@3306@train_business
drop table if exists `member`;
create table `member` (
  `id` bigint not null comment 'id',
  `mobile` varchar(11) comment '手机号',
  primary key (`id`),
  unique key `mobile_unique` (`mobile`)
) engine=innodb default charset=utf8mb4 comment='会员';
