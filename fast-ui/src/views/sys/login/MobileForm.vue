<template>
  <div v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="mobile" class="enter-x">
        <Input
          size="large"
          v-model:value="formData.mobile"
          :placeholder="t('sys.login.mobile')"
          class="fix-auto-fill"
        />
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <!-- 放置验证码和输入框 -->
        <CountdownInput
          size="large"
          :sendCodeApi="sendCode"
          class="fix-auto-fill"
          v-model:value="formData.sms"
          :placeholder="t('sys.login.smsCode')"></CountdownInput>
      </FormItem>
      <FormItem name="imgCode" class="enter-x">
        <a-input v-bind="$attrs"  v-model:value="formData.imgCode" size="large" placeholder="图片验证码" >
          <template #addonAfter>
            <img :src="captcha"  style="cursor: pointer" @click="doImgCaptcha" width="140" height="38" alt="图片验证码" >
          </template>
          <template #[item]="data" v-for="item in Object.keys($slots).filter((k) => k !== 'addonAfter')">
            <slot :name="item" v-bind="data || {}"></slot>
          </template>
        </a-input>
      </FormItem>

      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleLogin" :loading="loading">
          {{ t('sys.login.loginButton') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
  </div>
</template>
<script lang="ts" setup>
  import { reactive, ref, computed, unref } from 'vue';
  import {Form, Input, Button, notification,} from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useLoginState, useFormRules, useFormValid, LoginStateEnum } from './useLogin';
  import {imgCaptcha, smsLogin} from "@/api/sys/captcha";
  import {useUserStore} from "@/store/modules/user";
  const FormItem = Form.Item;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();
  const userStore = useUserStore();
  const formRef = ref();
  const loading = ref(false);
  const uuid = ref(null)
  const captcha = ref('')
  const formData = reactive({
    mobile: '',
    sms: '',
    imgCode: ''
  });

  const { validForm } = useFormValid(formRef);

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.MOBILE);

  async function sendCode() {
    const data = await unref(formRef).validateFields(['mobile','imgCode'])
    if (data){
      doImgCaptcha()
      await smsLogin(data.mobile, uuid.value, formData.imgCode)
      return true
    }
    return false
  }
  function doImgCaptcha(){
    imgCaptcha().then((res)=>{
      uuid.value = res.uuid
      captcha.value = res.img
      formData.imgCode = ''
    })
  }
  doImgCaptcha()
  async function handleLogin() {
    const data = await validForm();
    if (!data) return;
    const userInfo = await userStore.loginSms({
      phone: data.mobile,
      code: data.sms,
      uuid: uuid.value,
      imgCode: data.imgCode
    });
    if (userInfo) {
      notification.success({
        message: t('sys.login.loginSuccessTitle'),
        description: `${t('sys.login.loginSuccessDesc')}: ${userInfo.realName}`,
        duration: 3,
      });
    }
  }
</script>
