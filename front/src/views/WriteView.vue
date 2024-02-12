<script setup lang="ts">
import {ref} from "vue";

// const axios = require('axios').default;

import axios from "axios";

const count = ref(0) // 반응형으로 변수 만들기

const title = ref("")
const content = ref("")

const write = function () {
  console.log(title.value + " / " + content.value)
  axios.post("http://localhost:8080/posts", {
    title: title.value, content: content.value
  })
};
</script>

<template>
  <p>안녕하세요.</p> <button @click="count += 1">버튼을 눌러주세요.</button>
  <p>{{count}}</p>

  <div>
    <el-input type="text"  v-model="title" placeholder="제목을 입력해주세요."/>
  </div>

  <div>
    <div class="mt-2">
      <el-input type="textarea" v-model="content" rows="15"></el-input>
    </div>
    <div class="mt-2">
      <el-button type="primary" @click="write()">글 작성 완료!</el-button>
    </div>
  </div>


</template>

<style>

</style>