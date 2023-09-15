import { BasicColumn } from '/@/components/Table/src/types/table';

import { Badge } from 'ant-design-vue';
let getName = (id) =>{
    if(id == 1){
        return '国信（ETH）'
    }else if(id == 2){
        return '九道欢（JDH）'
    }else if(id == 3){
        return '欧妙舒（OMS）'
    }else if(id == 4){
        return '中星羽洋（ZXY）'
    }else if(id == 5){
        return '合凹好（HOH）'
    }else if(id == 6){
        return '游老大（YLD）'
    }else if(id == 7){
        return '皇御宴（HYY'
    }
}
//状态 0：买委托 1：已撤单 2：买成功 3：部分成交 4:已撤单，部分成交
let getStatus = (id) =>{
    if(id == 1){
        return '已撤单'
    }else if(id == 2){
        return '成功'
    }else if(id == 3){
        return '部分成交'
    }else if(id == 4){
        return '已撤单，部分成交'
    }else if(id == 0){
        return '买委托'
    }
}
export const positionListColumns: any[] = [

    {
        title: '资产名称',
        width: 150,
        dataIndex: 't3',
        customRender: ({ record }) => {
            return <Badge status="success" text={getName(record.tid)} />;
        },
    },
    {
        title: '总数量',
        width: 80,
        dataIndex: 'total',
    },
    {
        title: '冻结数量',
        width: 80,
        dataIndex: 'frozen',
    },
    {
        title: '成本价',
        width: 60,
        dataIndex: 'reqPrice',
    },
    {
        title: '更新时间',
        width: 150,
        dataIndex: 'updateTime',
    },

];
export const marketListColumns: any[] = [

    {
        title: '资产名称',
        dataIndex: 't3',
        customRender: ({ record }) => {
            return <Badge status="success" text={getName(record.market_id)} />;
        },
    },
    {
        title: '总数量',
        width: 80,
        dataIndex: 'num',
    }
];
export const orderSumColumns: any[] = [

    {
        title: '资产名称',
        width: 150,
        dataIndex: 't3',
        customRender: ({ record }) => {
            return <Badge status="success" text={getName(record.tid)} />;
        },
    },
    {
        title: '数量',
        width: 80,
        dataIndex: 'num',
    }
];
export const sellBuySumColumns: any[] = [

    {
        title: '资产名称',
        width: 150,
        dataIndex: 't3',
        customRender: ({ record }) => {
            return <Badge status="success" text={getName(record.tid)} />;
        },
    },
    {
        title: '委托数量',
        width: 80,
        dataIndex: 'total',
    },
    {
        title: '成交数量',
        width: 80,
        dataIndex: 'done',
    }
];
export const sellBuyColumns: any[] = [

    {
        title: '资产名称',
        width: 150,
        dataIndex: 't3',
        customRender: ({ record }) => {
            return <Badge status="success" text={getName(record.tid)} />;
        },
    },
    {
        title: '状态',
        width: 80,
        dataIndex: 'status',
        customRender: ({ record }) => {
            return <Badge status="success" text={getStatus(record.status)} />;
        },
    },
    {
        title: '委托数量',
        width: 80,
        dataIndex: 'total',
    },{
        title: '成交数量',
        width: 80,
            dataIndex: 'done',
    },

    {
        title: '委托价格',
        width: 80,
        dataIndex: 'reqPrice',
    },
    {
        title: '委托时间',
        width: 120,
        dataIndex: 'createTime',
    },
    {
        title: '更新时间',
        width: 120,
        dataIndex: 'updateTime',
    }
];

export const orderColumns: any[] = [

    {
        title: '资产名称',
        width: 150,
        dataIndex: 't3',
        customRender: ({ record }) => {
            return <Badge status="success" text={getName(record.tid)} />;
        },
    },
    {
        title: '买单ID',
        width: 80,
        dataIndex: 'buyId',
    },{
        title: '买单用户ID',
        width: 80,
        dataIndex: 'buyUid',
    },
    {
        title: '卖单ID',
        width: 80,
        dataIndex: 'sellId',
    },{
        title: '卖单用户ID',
        width: 80,
        dataIndex: 'sellUid',
    },
    {
        title: '交易数量',
        width: 80,
        dataIndex: 'num',
    },
    {
        title: '成本价',
        width: 80,
        dataIndex: 'reqPrice',
    },
    {
        title: '成交金额',
        width: 80,
        dataIndex: 'amount',
    },
    {
        title: '创建时间',
        width: 120,
        dataIndex: 'createTime',
    }
];

