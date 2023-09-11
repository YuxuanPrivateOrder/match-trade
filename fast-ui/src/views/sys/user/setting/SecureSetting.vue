<template>
  <CollapseContainer title="安全设置" :canExpan="false">
    <List>
      <template v-for="item in list" :key="item.key">
        <ListItem>
          <ListItemMeta>
            <template #title>
              {{ item.title }}
              <div class="extra" v-if="item.extra" @click="doExtra(item.eventKey)">
                {{ item.extra }}
              </div>
            </template>
            <template #description>
              <div v-if="item.key === 'phone'">已绑定手机：：{{ phone }}</div>
              <div v-else-if="item.key === 'email'">已绑定邮箱：：{{ email }}</div>
              <div v-else>{{item.description}}</div>
            </template>
          </ListItemMeta>
        </ListItem>
      </template>
    </List>
  </CollapseContainer>
</template>
<script lang="ts">
import {List} from 'ant-design-vue'
import {defineComponent, onMounted, ref} from 'vue'
import {CollapseContainer} from '/@/components/Container/index'

import {secureSettingList} from './data'
import {useMessage} from "@/hooks/web/useMessage";
import {getLoginUser} from "@/api/sys/user";


export default defineComponent({
  components: {CollapseContainer, List, ListItem: List.Item, ListItemMeta: List.Item.Meta},
  setup() {
    const {createMessage} = useMessage();
    const email = ref()
    const phone = ref()
    onMounted(async () => {
      const data = await getLoginUser()
      email.value = data.email
      phone.value = data.phone
    })
    function doExtra(key) {
      switch (key) {
        case 'updateMFA':
          // TODO MFA暂未实现
          createMessage.error('MFA暂未实现')
          break
      }
    }

    return {
      doExtra,
      email,
      phone,
      list: secureSettingList,
    }
  },
})
</script>
<style lang="less" scoped>
.extra {
  float: right;
  margin-top: 10px;
  margin-right: 30px;
  font-weight: normal;
  color: #1890ff;
  cursor: pointer;
}
</style>
