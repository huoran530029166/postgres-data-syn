postgres-data-syn项目简介：
该项目为针对于postgres数据库的数据同步工具。
lib-dataaccess：底层依赖项目，包含了数据库连接等方法的封装。
lib-datalogic：底层依赖项目，包含了一些通用逻辑处理方法的封装。
db-data-syn：postgres数据库数据同步项目。分为发送端和接收端。
			 发送端：根据指定数据库(模式或表)，生成sqlite数据文件，等同于数据库数据备份的过程。
			 接收端：根据指定sqlite数据文件和需要同步数据的数据库，将sqlite文件的数据同步到postgres数据库中，等同于数据库备份数据还原的过程。
			 