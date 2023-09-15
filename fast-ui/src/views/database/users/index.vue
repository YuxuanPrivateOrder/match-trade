<template>
  <PageWrapper title="工作辛苦了" contentBackground>
    <template #extra>
      <a-input  v-model:value="searchText" style="width: 200px" placeholder="请输入用户手机号或id搜索"></a-input>
      <a-button type="primary" @click="search" :loading="searchLoading"> 搜索 </a-button>
    </template>

    <template #footer>
      <a-tabs default-active-key="1" @change="search">
        <a-tab-pane key="1" tab="基础数据" >
          <div class="pt-4 m-4 desc-wrap" v-if="loadData">
            <a-descriptions size="small" :column="3">
              <a-descriptions-item label="真实姓名"> {{ user.realName || '未知'}} </a-descriptions-item>
              <a-descriptions-item label="手机号">{{user.user.userPhone}}</a-descriptions-item>
              <a-descriptions-item label="注册时间"> {{ dayjs(user.user.regTime * 1000).format('YYYY-MM-DD HH:mm:ss') }} </a-descriptions-item>
              <a-descriptions-item label="用户状态">
                <span v-if="user.user.loginStatus == 0">账户冻结</span>
                <span v-if="user.user.loginStatus == 1">账户正常</span>
              </a-descriptions-item>
              <a-descriptions-item label="交易状态">
                <span v-if="user.user.moneyStatus == 0">交易冻结</span>
                <span v-if="user.user.moneyStatus == 1">交易正常</span>
              </a-descriptions-item>
              <a-descriptions-item label="是否注销">
                <span v-if="user.user.isCancel == 0">未注销</span>
                <span v-if="user.user.isCancel == 1">已注销</span>
              </a-descriptions-item>
              <a-descriptions-item label="出金总数">
                {{ user.sumWithdrawal || 0}}
              </a-descriptions-item>
              <a-descriptions-item label="入金总数">
                {{ user.sumRecharge || 0}}
              </a-descriptions-item>
              <a-descriptions-item label="账户余额">
                {{ user.user.userMoney || 0}}
              </a-descriptions-item>
              <a-descriptions-item label="冻结资金">
                {{ user.user.userBalance || 0}}
              </a-descriptions-item>
            </a-descriptions>
            <Row :gutter="24">
              <Col :span="12">
              <ACard title='用户资产数据'>
                <Table size="small"  bordered emptyText="暂无资产数据" :pagination="false" :columns="positionListColumns" :data-source="user.positionList"></Table>
              </ACard>
              </Col>
              <Col :span="12">
                <ACard title="用户充值资产">
                  <Table size="small"  bordered emptyText="暂无资产数据" :pagination="false" :columns="marketListColumns" :data-source="user.sumRechargeMarket"></Table>
                </ACard>
              </Col>
            </Row>
          </div>
          <div v-else>
            <Empty />
          </div>
        </a-tab-pane>
        <a-tab-pane key="2" tab="用户买入(订单)" >
          <h3>数据汇总</h3>
          <Table size="small"  bordered emptyText="暂无数据" :pagination="false" :columns="orderSumColumns" :data-source="buyOrderData">
          </Table>
          <h3>详细数据</h3>
          <Table size="small" bordered emptyText="暂无数据" :pagination="false" :columns="orderColumns" :data-source="buyOrders">
          </Table>
        </a-tab-pane>
        <a-tab-pane key="4" tab="用户卖出(订单)" >
          <h3>数据汇总</h3>
          <Table size="small"  bordered emptyText="暂无数据" :pagination="false" :columns="orderSumColumns" :data-source="sellOrderData">
          </Table>
          <h3>详细数据</h3>
          <Table size="small" bordered emptyText="暂无数据" :pagination="false" :columns="orderColumns" :data-source="sellOrders">
          </Table>
        </a-tab-pane>
        <a-tab-pane key="3" tab="用户买入(买单)" >
          <h3>数据汇总</h3>
          <Table size="small"  bordered emptyText="暂无数据" :pagination="false" :columns="sellBuySumColumns" :data-source="buyData">
          </Table>
          <h3>详细数据</h3>
          <Table size="small" bordered emptyText="暂无数据" :pagination="false" :columns="sellBuyColumns" :data-source="user.buys">
          </Table>
        </a-tab-pane>
        <a-tab-pane key="5" tab="用户卖出(卖单)" >
          <h3>数据汇总</h3>
          <Table size="small"  bordered emptyText="暂无数据" :pagination="false" :columns="sellBuySumColumns" :data-source="sellData">
          </Table>
          <h3>详细数据</h3>
          <Table size="small" bordered emptyText="暂无数据" :pagination="false" :columns="sellBuyColumns" :data-source="user.sells">
          </Table>
        </a-tab-pane>
      </a-tabs>
    </template>


  </PageWrapper>
</template>
<script lang="ts">
import {defineComponent, reactive, ref} from 'vue';
import { PageWrapper } from '/@/components/Page';
import { Divider, Card, Empty, Descriptions, Steps, Tabs,Table,Row,Col } from 'ant-design-vue';
import {data} from '@/api/jys/users'
import {useMessage} from "@/hooks/web/useMessage";
import dayjs from "dayjs";
import {
  positionListColumns,
  marketListColumns,
  orderSumColumns,
  orderColumns, sellBuyColumns, sellBuySumColumns,
} from "@/views/database/users/data";

export default defineComponent({
  computed: {
    dayjs() {
      return dayjs
    }
  },
  components: {
    PageWrapper,
    Table,
    Row,Col,
    [Divider.name]: Divider,
    [Card.name]: Card,
    Empty,
    [Descriptions.name]: Descriptions,
    [Descriptions.Item.name]: Descriptions.Item,
    [Steps.name]: Steps,
    [Steps.Step.name]: Steps.Step,
    [Tabs.name]: Tabs,
    [Tabs.TabPane.name]: Tabs.TabPane,
  },
  setup() {
    const loadData = ref(false)
    const searchLoading = ref(false)
    const searchText = ref("")
    const { createMessage } = useMessage();
    const user = ref()
    const buyOrders = ref()
    const buyOrderData:any = reactive([])
    const sellOrders = ref()
    const sellOrderData:any = reactive([])
    const buyData:any = reactive([])
    const sellData:any = reactive([])
    let temp: {} = ref()
    function search(){
      if(!searchText.value){
        createMessage.error('请输入要查询的数据');
        return
      }
      searchLoading.value = true
      data(searchText.value).then(res=>{
        loadData.value = true
        user.value = res

        // buy orders = 用户的购买订单
        // 过滤出自己的买单
        buyOrders.value = res.orders.filter(item=>item.buyUid == res.user.userId)
        buyOrders.value.forEach(order=>{
          if(temp.hasOwnProperty(order.tid)){
            temp[order.tid] = temp[order.tid] + order.num
          }else {
            temp[order.tid] = order.num
          }
        })
        buyOrderData.splice(0,buyOrderData.length)
        Object.keys(temp).forEach(k=>{
          if(!isNaN(Number(k))){
            buyOrderData.push({tid:k,num:temp[k]})
          }
        })

        // sell orders = 用户的购买订单
        sellOrders.value = res.orders.filter(item=>item.sellUid == res.user.userId)
        temp = {}
        sellOrders.value.forEach(order=>{
          if(temp.hasOwnProperty(order.tid)){
            temp[order.tid] = temp[order.tid] + order.num
          }else {
            temp[order.tid] = order.num
          }
        })
        sellOrderData.splice(0,sellOrderData.length)
        Object.keys(temp).forEach(k=>{
          if(!isNaN(Number(k))){
            sellOrderData.push({tid:k,num:temp[k]})
          }
        })
        buyData.splice(0,sellData.length)
        // 聚合一下用户卖单的金额 buyData
        res.buys.forEach(function (item) {
          if (buyData[item.tid]) {
            buyData[item.tid].total += item.total;
            buyData[item.tid].done += item.done;
          } else {
            buyData[item.tid] = {
              tid: item.tid,
              total: item.total,
              done: item.done
            };
          }
        });
        sellData.splice(0,sellData.length)
        res.sells.forEach(function (item) {
          if (sellData[item.tid]) {
            sellData[item.tid].total += item.total;
            sellData[item.tid].done += item.done;
          } else {
            sellData[item.tid] = {
              tid: item.tid,
              total: item.total,
              done: item.done
            };
          }
        });

      }).finally(()=>{
        searchLoading.value = false
      })
    }

    return {
      searchLoading,
      loadData,
      search,
      searchText,
      user,
      buyOrders,
      buyOrderData,
      sellOrders,
      sellOrderData,
      sellBuyColumns,
      positionListColumns,
      marketListColumns,
      orderSumColumns,
      orderColumns,
      sellData,
      buyData,sellBuySumColumns
    };
  },
});
</script>