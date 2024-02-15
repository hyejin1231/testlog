<script setup lang="ts">
import {defineProps, ref} from "vue";
import {useRouter} from "vue-router";
import axios from "axios";

const router = useRouter()


const post = ref({
  id : 0,
  title : "",
  content : ""
});

const props = defineProps({
  postId : {
    type: [Number, String], require : true
  }
})

axios.get(`/api/posts/${props.postId}`).then((response) => {
  console.log(response);
  post.value = response.data;
});

const edit = () => {
  axios.patch(`/api/posts/${props.postId}`, post.value).then((response) => {
    router.replace({name: "home"})
  });
}

</script>

<template>

  <div>
    <el-input type="text"  v-model="post.title" placeholder="제목을 입력해주세요."/>
  </div>

  <div>
    <div class="mt-2">
      <el-input type="textarea" v-model="post.content" rows="15"></el-input>
    </div>
    <div class="mt-2 d-flex justify-content-end">
      <el-button type="warning" @click="edit()">수정 완료!</el-button>
    </div>
  </div>


</template>

<style>

</style>