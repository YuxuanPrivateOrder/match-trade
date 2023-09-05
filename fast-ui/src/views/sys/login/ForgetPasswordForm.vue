<template>
  <template v-if="getShow">
    <LoginFormTitle class="enter-x" />
    <Form class="p-4 enter-x" :model="formData" :rules="getFormRules" ref="formRef">
      <FormItem name="mobile" class="enter-x">
        <Input size="large" v-model:value="formData.mobile" :placeholder="t('sys.login.mobile')" />
      </FormItem>
      <FormItem name="sms" class="enter-x">
        <CountdownInput
          size="large"
          :sendCodeApi="sendCode"
          v-model:value="formData.sms"
          :placeholder="t('sys.login.smsCode')"
        />
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
      <FormItem name="password" class="enter-x">
        <StrengthMeter
            size="large"
            v-model:value="formData.password"
            :placeholder="t('sys.login.password')"
        />
      </FormItem>
      <FormItem name="confirmPassword" class="enter-x">
        <InputPassword
            size="large"
            visibilityToggle
            v-model:value="formData.confirmPassword"
            :placeholder="t('sys.login.confirmPassword')"
        />
      </FormItem>
      <FormItem class="enter-x">
        <Button type="primary" size="large" block @click="handleReset" :loading="loading">
          {{ t('common.resetText') }}
        </Button>
        <Button size="large" block class="mt-4" @click="handleBackLogin">
          {{ t('sys.login.backSignIn') }}
        </Button>
      </FormItem>
    </Form>
  </template>
</template>
<script lang="ts" setup>
  import { reactive, ref, computed, unref } from 'vue';
  import LoginFormTitle from './LoginFormTitle.vue';
  import { Form, Input, Button } from 'ant-design-vue';
  import { CountdownInput } from '/@/components/CountDown';
  import { useI18n } from '/@/hooks/web/useI18n';
  import { useLoginState, useFormRules, LoginStateEnum } from './useLogin';
  import {StrengthMeter} from "@/components/StrengthMeter";
  import {imgCaptcha, smsChangePassword} from "@/api/sys/captcha";

  const FormItem = Form.Item;
  const InputPassword = Input.Password;
  const { t } = useI18n();
  const { handleBackLogin, getLoginState } = useLoginState();
  const { getFormRules } = useFormRules();

  const formRef = ref();
  const loading = ref(false);
  const uuid = ref(null)
  const captcha = ref('')
  const formData = reactive({
    mobile: '',
    sms: '',
    password: '',
    confirmPassword: '',
    imgCode: ''
  });
  function doImgCaptcha(){
    imgCaptcha().then((res)=>{
      uuid.value = res.uuid
      captcha.value = res.img
      formData.imgCode = ''
    })
  }
  doImgCaptcha()

  async function sendCode() {
    const data = await unref(formRef).validateFields(['mobile','imgCode'])
    if (data){
      doImgCaptcha()
      await smsChangePassword(data.mobile, uuid.value, formData.imgCode)
      return true
    }
    return false
  }

  const getShow = computed(() => unref(getLoginState) === LoginStateEnum.RESET_PASSWORD);

  async function handleReset() {
    const form = unref(formRef);
    if (!form) return;
    const data = await form.validate();

    if (!data) return;
    loading.value = true;
    setTimeout(() => {
      loading.value = false;
      alert(1)
    }, 2000);

  }
</script>
