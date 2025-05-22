import { defineConfig } from 'vite';
import { resolve } from 'path';

export default defineConfig({
  resolve: {
    alias: {
      'Frontend/generated': resolve(__dirname, 'generated')
    }
  }
}); 