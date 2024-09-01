<template>
  <div class="login-container">
    <el-form
      :model="loginForm"
      ref="formRef"
      label-width="80px"
      class="login-form"
      :rules="loginRules"
    >
      <h2>User Login</h2>
      <el-form-item label="Username" prop="username">
        <el-input
          v-model="loginForm.username"
          ref="username"
          name="username"
          autocomplete="off"
          placeholder="Enter the username"
        ></el-input>
      </el-form-item>
      <el-form-item label="Password&nbsp" prop="userPwd">
        <el-input
          type="password"
          v-model="loginForm.userPwd"
          autocomplete="off"
          placeholder="Enter the password"
        ></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="success" @click.native.prevent="login"
          >Sign in</el-button
        >
        <el-button type="primary" @click="toRegister">Create account</el-button>
      </el-form-item>
    </el-form>
  </div>
</template>

<script lang="ts">
import { defineComponent } from "vue";
export default defineComponent({
  name: "Login",
});
</script>
<script lang="ts" setup>
import { ref } from "vue";
import { useUserInfoStore } from "../../stores/userInfo";

import type { FormInstance } from "element-plus";
import { useRouter } from "vue-router";
const userInfoStore = useUserInfoStore();
const router = useRouter();
const formRef = ref<FormInstance>();
const loading = ref(false);
//账号密码参数
const loginForm = ref({
  username: "zhangsan",
  userPwd: "123456",
});
// 校验规则
const validateUsername = (rule: any, value: any, callback: any) => {
  if (value.length < 4) {
    callback(new Error("At least 4 characters is required"));
  } else {
    callback();
  }
};
// 校验规则
const validatePassword = (rule: any, value: any, callback: any) => {
  if (value.length < 6) {
    callback(new Error("At least 6 characters is required"));
  } else {
    callback();
  }
};
// 校验规则
const loginRules = {
  username: [{ required: true, validator: validateUsername }],
  userPwd: [{ required: true, trigger: "blur", validator: validatePassword }],
};
//点击登录的回调
const login = async () => {
  // console.log('点击登录');
  await formRef.value?.validate();
  loading.value = true;
  try {
    // await getUserInfo(loginForm.value)
    await userInfoStore.login(loginForm.value);
    router.push({ name: "HeadlineNews" });
  } finally {
    loading.value = false;
  }
  // loading.value = true
  // const { username, userPwd } = loginForm.value
  // try {
  //   await userInfoStore.login(username, userPwd)
  //   router.push({ path: redirect.value || '/' })
  // } finally {
  //   loading.value = false
  // }
};

const toRegister = () => {
  router.push({ name: "Register" });
};
</script>

<style scoped>
.login-container {
  background-color: rgb(241, 240, 240);
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;

  background-image: url("../../assets/city1.jpg");
  background-size: cover; /* Scales the image to cover the entire element */
  background-repeat: no-repeat; /* Prevents the image from repeating */
  background-position: center; /* Centers the image */
  height: 100vh; /* Ensures the body takes up the full viewport height */
  margin: 0; /* Removes default body margin */
}
.login-form {
  width: 450px;
  text-align: center;
  border-style: solid;
  border-color: rgba(200, 201, 202, 0.803);
  border-radius: 5%;
  background-color: rgb(190, 197, 200);
  padding-right: 10px;
  opacity: 0.95;
}
.el-form-item__label {
  display: inline-flex;
  justify-content: flex-start;
  align-items: flex-start;
  flex: 0 0 auto;
  font-size: var(--el-form-label-font-size);
  font-weight: bolder;
  color: var(--el-text-color-regular);
  height: 32px;
  line-height: 32px;
  padding: 0 12px 0 0;
  box-sizing: border-box;
}
h2 {
  font-family: "Lucida Sans", "Lucida Sans Regular", "Lucida Grande",
    "Lucida Sans Unicode", Geneva, Verdana, sans-serif;
  font-weight: bolder;
}
.el-button {
  margin-left: 30px;
}
.el-button--success {
  margin-right: 20px;
}
</style>
